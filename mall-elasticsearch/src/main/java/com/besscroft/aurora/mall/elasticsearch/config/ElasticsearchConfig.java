package com.besscroft.aurora.mall.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.time.Duration;

@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                // 单机配置
                .connectedTo("127.0.0.1:9200")
                // 集群配置
//                .connectedTo("localhost:9200", "localhost:9291")
                // 启用 SSL (可选)
//                .usingSsl()
                // 设置代理
//                .withProxy("localhost:1080")
                // 设置连接超时,默认值为10秒.
                .withConnectTimeout(Duration.ofSeconds(5))
                // 设置套接字超时,默认值为5秒.
                .withSocketTimeout(Duration.ofSeconds(3))
                // 设置基本身份验证
//                .withBasicAuth(username, password)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

}
