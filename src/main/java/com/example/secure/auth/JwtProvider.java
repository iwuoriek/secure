package com.example.secure.auth;

import com.example.secure.entities.Role;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private final String ROLES_KEY = "roles";
    private JwtParser parser;

    private String secretKey;
    private long validityInMilliseconds;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);

    @Autowired
    private JwtProvider(@Value("${security.jwt.secret-key}") String secretKey,
                        @Value("${security.jwt.token.expiration}") long validity){
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validity;
    }

    public String createToken(String username, Set<Role> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_KEY, roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            LOGGER.info("Token validation is successful");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.warn("Token validation failed");
            return false;
        }
    }

    public String getUsername(String token){
        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }

    public Set<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>> roleClaims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(ROLES_KEY, List.class);

        return roleClaims
                .stream()
                .map(roleClaim -> new SimpleGrantedAuthority(roleClaim.get("authority")))
                .collect(Collectors.toSet());
    }
}
