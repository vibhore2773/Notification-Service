package com.meesho.NotificationService.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.meesho.NotificationService.repository")
public class ElasticsearchConfig {
    @Value("${elasticsearch.port}")
    private String hostAndPort;
    @Bean
    public RestHighLevelClient client(){
        ClientConfiguration configuration = ClientConfiguration.builder().connectedTo(hostAndPort).build();
        return RestClients.create(configuration).rest();
    }

    @Bean
    ElasticsearchOperations elasticsearchTemplate(){
        return new ElasticsearchRestTemplate(client());
    }
}
