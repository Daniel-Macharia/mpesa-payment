package com.tribesystems.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi tvet360API()
    {
        return GroupedOpenApi.builder()
                .group("TRIBE_PAYMENT")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOPenApi()
    {
        return new OpenAPI()
                .info(new Info().title("Tribe Payment API").version("1.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8448").description("local development server"),
                        new Server().url("https://www.tribessystems.co.ke").description("remote test server")
                ));
    }
}
