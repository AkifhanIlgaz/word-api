package com.zozak.word_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.accessToken.expiration}")
    public long accessTokenExpiration;
    @Value("${application.security.jwt.refreshToken.expiration}")
    public long refreshTokenExpiration;

    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS -> generateAccessToken(userDetails);
            case REFRESH -> generateRefreshToken(userDetails);
        };
    }

    private String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256 )
                .compact();
    }

    private String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256 )
                .compact();
    }

    public <T> T parseToken(String token, Function<Claims, T> claimsFunction) {
        Claims claims = getClaimsFromToken(token);

        return claimsFunction.apply(claims);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return parseToken(token, Claims::getSubject);
    }

    public boolean isExpired(String token) {
        Date expiresAt = parseToken(token, Claims::getExpiration);
        return expiresAt.before(new Date());
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
