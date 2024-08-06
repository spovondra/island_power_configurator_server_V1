package com.islandpower.configurator.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * This class provides methods to generate, parse, and validate JWT tokens.
 *
 * @version 1.0
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing JWT tokens.
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Converts the secret key from a string to a SecretKey object.
     *
     * @return SecretKey - The converted secret key
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token - The JWT token from which to extract the username
     * @return String - The username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token - The JWT token from which to extract the expiration date
     * @return Date - The expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token - The JWT token from which to extract the claim
     * @param claimsResolver - Function to resolve the desired claim from the Claims object
     * @param <T> - The type of the claim to extract
     * @return T - The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token to extract all claims.
     *
     * @param token - The JWT token to parse
     * @return Claims - The claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token - The JWT token to check
     * @return Boolean - True if the token has expired, otherwise false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a new JWT token for the given user details.
     *
     * @param userDetails - The user details for which to generate the token
     * @return String - The generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }

    /**
     * Creates a JWT token with the specified subject (username).
     *
     * @param subject - The subject to include in the token
     * @return String - The created JWT token
     */
    private String createToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token by checking its username and expiration.
     *
     * @param token - The JWT token to validate
     * @param userDetails - The user details to check against the token
     * @return Boolean - True if the token is valid, otherwise false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
