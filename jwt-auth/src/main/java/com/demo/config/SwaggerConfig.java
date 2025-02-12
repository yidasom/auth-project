package com.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * com.demo.config
 *
 * @author : idasom
 */
// http://localhost:8081/swagger-ui.html
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("jwt auth API")
                .description("jwt 인증")
                .version("1.0.0")
        );
    }

}
