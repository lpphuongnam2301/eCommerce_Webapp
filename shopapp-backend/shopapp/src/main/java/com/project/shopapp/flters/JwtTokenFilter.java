package com.project.shopapp.flters;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        try {
            if (isBypass(request))
            {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer "))
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AuthHeader null or not started with Bearer");
            }
            if(authHeader.startsWith("Bearer "))
            {
                final String token = authHeader.substring(7);
                final String phoneNumber = jwtTokenUtil.getPhoneNumber(token);
                if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    User user = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if(jwtTokenUtil.validateToken(token, user))
                    {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        }
    }
    @Value("${api.prefix}")
    private String apiPrefix;
    private boolean isBypass(@NonNull HttpServletRequest request)
    {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(apiPrefix+"/products", "GET"),
                Pair.of(apiPrefix+"/categories", "GET"),
                Pair.of(apiPrefix+"/users/register", "POST"),
//                Pair.of(apiPrefix+"/products/images/**", "GET"),
                Pair.of(apiPrefix+"/users/login", "POST")
        );
        for(Pair<String, String> bypass : byPassTokens)
        {
            if (request.getServletPath().contains(bypass.getFirst()) &&
                    request.getMethod().equals(bypass.getSecond()))
            {
                return true;
            }
        }
        return false;
    }
}
