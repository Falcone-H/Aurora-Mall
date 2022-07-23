package com.besscroft.aurora.mall.auth.controller;

import com.besscroft.aurora.mall.common.constant.AuthConstants;
import com.besscroft.aurora.mall.common.domain.Oauth2Token;
import com.besscroft.aurora.mall.common.result.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.*;

/**
 * 参考文档：https://docs.spring.io/spring-security/oauth/apidocs/org/springframework/security/oauth2/provider/endpoint/TokenEndpoint.html
 * <p>
 * https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide
 * <p>
 * 这里也算是对TokenEndpoint包装了一层Controller
 */
@Slf4j
@Api(tags = "商城认证中心接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenEndpoint tokenEndpoint;
    private final Set<HttpMethod> allowedRequestMethods = new HashSet<>(Arrays.asList(HttpMethod.POST, HttpMethod.GET));

    @ApiOperation("根据当前用户的信息生成token")
    @GetMapping("/oauth/token")
    public Oauth2Token getAccessToken(
            Principal principal, @RequestParam Map<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        log.info("GET /oauth/token");
        if (!allowedRequestMethods.contains(HttpMethod.GET)) {
            throw new HttpRequestMethodNotSupportedException("GET");
        }
        return postAccessToken(principal, parameters);
    }

    @ApiOperation("OAuth2认证生成token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID", required = true),
            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", value = "登录用户名"),
            @ApiImplicitParam(name = "password", value = "登录密码")
    })
    @PostMapping("/oauth/token")
    public Oauth2Token postAccessToken(
            @ApiIgnore Principal principal,
            @ApiIgnore @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException {
        log.info("POST /oauth/token");
        log.info("请求到了，parameters:{}", parameters);
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

//        User clientUser = new User("admin-app", "123456", new ArrayList<>());
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientUser, null, new ArrayList<>());
//        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(token, parameters).getBody();

        assert oAuth2AccessToken != null;
        Oauth2Token oauth2Token = Oauth2Token.builder()
                .access_token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead(AuthConstants.JWT_TOKEN_PREFIX).build();
        log.info("生成的token:{}", oauth2Token);
        return oauth2Token;
    }
}







