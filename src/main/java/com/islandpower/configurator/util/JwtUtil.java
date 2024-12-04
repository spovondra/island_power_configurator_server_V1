package com.islandpower.configurator.util;

import com.islandpower.configurator.exceptions.TokenExpiredException;
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
 * Provides methods for generating, validating, extracting claims, and refreshing JWT tokens.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey; //secret key for signing JWT tokens

    @Value("${jwt.expiration}")
    private long accessTokenExpirationMs; //expiration time for access tokens (ms)

    @Value("${jwt.refreshExpirationMs}")
    private long refreshTokenExpirationMs; //expiration time for refresh tokens (ms)

    /**
     * Returns the signing key used to create and verify JWT tokens.
     *
     * @return SecretKey The signing key
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return String The username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token
     * @return Date the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claim resolver.
     *
     * @param token The JWT token
     * @param claimsResolver A function to resolve the claim from the token
     * @param <T> The type of the claim
     * @return The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token
     * @return Claims The claims extracted from the token
     * @throws TokenExpiredException If the token has expired
     * @throws RuntimeException If the token is invalid
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new TokenExpiredException("JWT token has expired.", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token.", e);
        }
    }

    /**
     * Checks if the given JWT token has expired.
     *
     * @param token The JWT token
     * @return Boolean True if the token is expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates an access token for the given user details.
     *
     * @param userDetails The user details to generate the token for
     * @return String The generated JWT access token
     */
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), accessTokenExpirationMs);
    }

    /**
     * Generates a refresh token for the given user details.
     *
     * @param userDetails The user details to generate the refresh token for
     * @return String The generated JWT refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername(), refreshTokenExpirationMs);
    }

    /**
     * Creates a JWT token for the specified subject (username) and expiration time.
     *
     * @param subject The subject (username) of the token
     * @param expirationMs The expiration time in milliseconds
     * @return String The generated JWT token
     */
    private String createToken(String subject, long expirationMs) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT token by checking the username and expiration.
     *
     * @param token The JWT token to validate
     * @param userDetails The user details to check against
     * @return Boolean True if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT refresh token is refreshable (but not expired!!).
     *
     * @param token The JWT refresh token
     * @return Boolean True if the refresh token is not expired, false otherwise
     */
    public Boolean isTokenRefreshable(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Refreshes the given refresh token and generates a new access token.
     *
     * @param refreshToken The JWT refresh token
     * @return String the new access token
     * @throws TokenExpiredException If the refresh token is expired
     */
    public String refreshToken(String refreshToken) {
        if (isTokenRefreshable(refreshToken)) {
            String username = extractUsername(refreshToken);
            return createToken(username, accessTokenExpirationMs);
        }
        throw new com.islandpower.configurator.exceptions.TokenExpiredException("Refresh token is expired.");
    }
}
