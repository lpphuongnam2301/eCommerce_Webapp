package com.project.shopapp.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.project.shopapp.models.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(User user)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        //generateSecretKey();
        try
        {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSecretkey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private String generateSecretKey()
    {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Key getSecretkey()
    {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSecretkey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimTFunction)
    {
        Claims claims = this.extractAllClaims(token);
        return claimTFunction.apply(claims);
    }

    public boolean isTokenExpired(String token)
    {
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String getPhoneNumber(String token)
    {
        return this.extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        String phoneNumber = getPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
