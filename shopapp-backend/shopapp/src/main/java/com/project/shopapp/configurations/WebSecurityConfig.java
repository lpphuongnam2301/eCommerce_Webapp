package com.project.shopapp.configurations;

import com.project.shopapp.flters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
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
                            .requestMatchers(HttpMethod.GET, apiPrefix + "/orders/**").hasAnyRole(Role.user, Role.admin)
                            .requestMatchers(HttpMethod.POST, apiPrefix + "/orders/**").hasRole(Role.user)
                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/orders/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/orders/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.GET, apiPrefix + "/categories**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(HttpMethod.POST, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/categories/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.GET, apiPrefix + "/products**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(HttpMethod.POST, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/products/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.GET, apiPrefix + "/order_details**").hasAnyRole(Role.admin, Role.user)
                            .requestMatchers(HttpMethod.POST, apiPrefix + "/order_details/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.PUT, apiPrefix + "/order_details/**").hasRole(Role.admin)
                            .requestMatchers(HttpMethod.DELETE, apiPrefix + "/order_details/**").hasRole(Role.admin);;
                });
        return httpSecurity.build();
    }
}
