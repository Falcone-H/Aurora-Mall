package com.besscroft.aurora.mall.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2Token {

    @ApiModelProperty("访问令牌")
    private String access_token;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("访问令牌头前缀")
    private String tokenHead;

    @ApiModelProperty("有效时间(秒)")
    private int expiresIn;

}
