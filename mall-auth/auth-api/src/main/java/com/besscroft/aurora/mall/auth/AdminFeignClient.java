package com.besscroft.aurora.mall.auth;

import com.besscroft.aurora.mall.auth.fallback.AdminFeignFallbackClient;
import com.besscroft.aurora.mall.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall-admin", fallback = AdminFeignFallbackClient.class)
public interface AdminFeignClient {

    /**
     * 根据用户名获取用户登录信息
     * @param username 用户名
     * @return 用户登录信息
     */
    @GetMapping("/user/loadByUsername")
    UserDto loadUserByUsername(@RequestParam String username);

}
