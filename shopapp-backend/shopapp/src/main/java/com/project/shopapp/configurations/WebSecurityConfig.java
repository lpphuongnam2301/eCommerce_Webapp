package com.project.shopapp.configurations;

import com.project.shopapp.flters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(apiPrefix + "/users/register", apiPrefix + "/users/login").permitAll()
                            .requestMatchers(GET, apiPrefix + "/orders/**").hasAnyRole(Role.user, Role.admin)
                            .requestMatchers(POST, apiPrefix + "/orders/**").hasRole(Role.user)
                            .requestMatchers(PUT, apiPrefix + "/orders/**").hasRole(Role.admin)
                            .requestMatchers(DELETE, apiPrefix + "/orders/**").hasRole(Role.admin)
                            .requestMatchers(GET, apiPrefix + "/categories**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(POST, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(PUT, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(DELETE, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(GET, apiPrefix + "/products**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(POST, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(PUT, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(DELETE, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(GET, apiPrefix + "/order_details**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(POST, apiPrefix + "/order_details/**").hasRole(Role.admin)
                            .requestMatchers(PUT, apiPrefix + "/order_details/**").hasRole(Role.admin)
                            .requestMatchers(DELETE, apiPrefix + "/order_details/**").hasRole(Role.admin).anyRequest().authenticated();
                }).csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return httpSecurity.build();
    }
}
