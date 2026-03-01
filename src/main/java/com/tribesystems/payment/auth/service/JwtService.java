package com.tribesystems.payment.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${payment.secret-key}")
    private String secretKey;

    @Value("${payment.jwt-issuer-name}")
    private String jwtIssuerName;


    public String generateToken(UserDetails userDetails)
    {
        HashMap<String, String> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuer(getJwtIssuerName())
                .issuedAt(new Date(System.currentTimeMillis()))
                //let token never expire since it will be used by other systems for data insertion and update
//                .expiration(new Date( System.currentTimeMillis() + (60 * 60 * 1000)))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private String getSecretKey(){return this.secretKey;}
    private String getJwtIssuerName(){return this.jwtIssuerName;}

    private SecretKey generateKey()
    {
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }

    public String extractUsername(String jwtToken)
    {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaims(String jwtToken, Function<Claims, T> claimsResolver)
    {
        Claims claims = extractClaims(jwtToken);

        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String jwtToken)
    {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails)
    {
        String username = extractUsername(jwtToken);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    public boolean isTokenExpired(String jwtToken)
    {
        return extractExpirationDate(jwtToken).before(new Date());
    }

    private Date extractExpirationDate(String jwtToken)
    {
        return extractClaims(jwtToken, Claims::getExpiration);
    }
}
