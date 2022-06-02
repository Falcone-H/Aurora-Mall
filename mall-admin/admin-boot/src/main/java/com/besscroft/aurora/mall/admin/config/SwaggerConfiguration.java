package com.besscroft.aurora.mall.admin.config;

import com.besscroft.aurora.mall.common.config.BaseSwaggerConfig;
import com.besscroft.aurora.mall.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 */
@Configuration
@EnableSwagger2
@Profile(value = {"dev"})
public class SwaggerConfiguration extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.besscroft.aurora.mall.admin.controller")
                .title("开发文档")
                .description("后台相关接口文档")
                .enableSecurity(true)
                .build();
    }

}
