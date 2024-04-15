package com.project.shopapp.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Api in Java Spring boot",
                version = "1.0.0",
                description = "E-Commerce"
        ),
        servers = {
                @Server(url = "http://localhost:8088", description = "Local Development Server"),
                @Server(url = "http://192.168.1.235:8088", description = "Production Server"),
        }
)

@SecurityScheme(
        name = "bearer-key", 
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

}
