package com.celsodiehl.todosimples.security;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getKeyBySecret() {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    //COLOQUEI POR ESTA OBSOLETA Depreciation
    @SuppressWarnings("deprecation")
    public String generateToken(String username) {
        SecretKey key = getKeyBySecret();
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key)
                .compact();
    }

    // Verificar sse o Token é válido
    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate))
                return true;
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims))
            return claims.getSubject();
        return null;
    }

    @SuppressWarnings("deprecation")
    private Claims getClaims(String token) {
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
