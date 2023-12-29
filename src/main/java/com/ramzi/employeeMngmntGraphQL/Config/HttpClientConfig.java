package com.ramzi.employeeMngmntGraphQL.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;

@Configuration
public class HttpClientConfig {
    @Bean
    public HttpGraphQlClient httpGraphQlClient(){
        return HttpGraphQlClient.builder().url("http://localhost:8080/graphiql?path=/graphql").build();
    }
}
