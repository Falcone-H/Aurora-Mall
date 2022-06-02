package com.besscroft.aurora.mall.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 网关白名单配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix="whitelist")
public class IgnoreUrlsConfig {

    private List<String> urls;

}
