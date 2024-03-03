package com.embedcraft.embedcraftcore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for JSON Web Token (JWT) operations including token generation, parsing, and validation.
 * It provides methods to create and verify JWT tokens using the HS256 signature algorithm.
 */
@Slf4j
public class JWTUtil {
    public static final String JWT_KEY = "123456";

    public static final long JWT_EXPIRY_DURATION = 30 * 60 * 1000L; // 30 minutes in milliseconds

    public static List<String> blacklist = new ArrayList<>();

    /**
     * Encodes the JWT secret key using Base64 and creates a {@link SecretKey}.
     *
     * @return The encoded {@link SecretKey}.
     */
    private static SecretKey getSecretKey() {
        // Assuming JWT_KEY is your secret string
        // Adjust this to use a more secure way of generating and storing keys
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes());
    }

    /**
     * Generates a JWT token for the given admin ID.
     *
     * @param adminId The ID of the admin for whom the token is to be generated.
     * @return A string representation of the JWT token.
     */
    public static String createJWT(Integer adminId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + JWT_EXPIRY_DURATION;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        // Generate the JWT token
        try {
            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setSubject(JSONUtil.serialize(adminId))
                    .setIssuer("system")
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (JsonProcessingException e) {
            log.error("JSON serialization error during JWT creation", e);
            return null;
        }
    }


    /**
     * Parses a JWT token to extract the claims.
     *
     * @param token The JWT token to be parsed.
     * @return The {@link Claims} contained within the token.
     */
    public static Claims parseJWT(String token) {
        return Jwts.parserBuilder()  // Updated to use parserBuilder()
                .setSigningKey(getSecretKey()) // No change here, but method is now on the builder
                .build() // Build the JwtParser instance
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Parses a JWT token and deserializes the subject into a specific class type.
     *
     * @param token The JWT token to be parsed.
     * @param clazz The class type to deserialize the subject into.
     * @param <T>   The type of the class.
     * @return An instance of {@code T} with fields populated from the token's subject.
     */
    public static <T> T parseJWT(String token, Class<T> clazz) {
        Claims body = parseJWT(token);
        try {
            return JSONUtil.deserialize(body.getSubject(), clazz);
        } catch (IOException e) {
            log.error("Error deserializing token subject into class", e);
            throw new RuntimeException("Token parsing error", e);
        }
    }

    /**
     * Adds a token to a blacklist, effectively invalidating it for future use.
     *
     * @param token The JWT token to be invalidated.
     */
    public static void invalidateJWT(String token) {
        blacklist.add(token);
    }

    /**
     * Checks if a token is in the blacklist.
     *
     * @param token The JWT token to check.
     * @return {@code true} if the token is in the blacklist, otherwise {@code false}.
     */
    public static boolean isInBlacklist(String token) {
        return blacklist.contains(token);
    }


}