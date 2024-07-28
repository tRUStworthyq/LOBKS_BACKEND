package com.project.lobks.security.jwt;

import com.project.lobks.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String accessSecret;
    @Value("${app.jwt.expirationMs}")
    private Long expiration;


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
    }
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token is expired");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("JWT token is invalid");
        } catch (SignatureException e) {
            throw new RuntimeException("JWT signature length not correct");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT string claims is empty");
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
