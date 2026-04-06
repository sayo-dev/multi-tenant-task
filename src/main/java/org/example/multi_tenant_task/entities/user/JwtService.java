package org.example.multi_tenant_task.entities.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.multi_tenant_task.exception.JwtExpiredException;
import org.example.multi_tenant_task.util.TokenPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {


    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expirationMS;

    @Value("${app.jwt.refresh-expiration}")
    private Long refreshExpirationMS;

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        return new TokenPair(accessToken, refreshToken);
    }


    public String generateRefreshToken(Authentication authentication) {

        return generateToken(authentication, refreshExpirationMS, Map.of("tokenType", "refresh"));
    }

    public String generateAccessToken(Authentication authentication) {

        return generateToken(authentication, expirationMS, Map.of());
    }

    public String generateToken(Authentication authentication, long expiration, Map<String, String> claims) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date iat = new Date();
        Date exp = new Date(iat.getTime() + expiration);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(signWith())
                .compact();
    }

    public String extractUsernameFromToken(String token) {

        Claims claims = extractClaims(token);
        if (claims == null) return null;

        return claims.getSubject();

    }

    public boolean isTokenValidForUser(String token, UserDetails userDetails) {

        String username = extractUsernameFromToken(token);

        return username != null && username.equals(userDetails.getUsername());

    }

    public boolean isTokenValid(String token) {

        return extractClaims(token) != null;
    }

    public boolean isRefreshToken(String token) {

        Claims claims = extractClaims(token);
        if (claims == null) return false;

        return "refresh".equals(claims.get("tokenType"));
    }

    public Claims extractClaims(String token) {

        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(signWith())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtExpiredException("Jwt expired or invalid");
        }

        return claims;
    }

    public long getRemainingValidity(String token) {
        Claims claims = extractClaims(token);

        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }


    private SecretKey signWith() {
        byte[] keyByte = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
