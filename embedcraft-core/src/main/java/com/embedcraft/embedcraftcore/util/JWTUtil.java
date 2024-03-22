package com.embedcraft.embedcraftcore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Utility class for JSON Web Token (JWT) operations including token generation, parsing, and validation.
 * It provides methods to create and verify JWT tokens using the HS256 signature algorithm.
 */
@Slf4j
public class JWTUtil {
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure key generation

    public static final long JWT_EXPIRY_DURATION = 30 * 60 * 1000L; // 30 minutes in milliseconds

    public static List<String> blacklist = new ArrayList<>();

//    //    /**
////     * Encodes the JWT secret key using Base64 and creates a {@link SecretKey}.
////     *
////     * @return The encoded {@link SecretKey}.
////     */
//    private static SecretKey getSecretKey() {
//        // Convert the secret string key into a SecretKey object
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }

    /**
     * Generates a JWT token for the given user ID.
     *
     * @param userId The ID of the user for whom the token is to be generated.
     * @return A string representation of the JWT token.
     */
    public static String createJWT(Integer userId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + JWT_EXPIRY_DURATION;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        // Generate the JWT token
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secretKey)
                .compact();

    }

    /**
     * Parses the given JWT token to extract the user ID.
     *
     * @param token The JWT token to be parsed.
     * @return The user ID as an Integer, or null if the token is invalid or the user ID cannot be extracted.
     */
    public static Integer parseUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }


//    /**
//     * Parses a JWT token to extract the claims.
//     *
//     * @param token The JWT token to be parsed.
//     * @return The {@link Claims} contained within the token.
//     */
//    public static Claims parseJWT(String token) {
//        return Jwts.parserBuilder()  // Updated to use parserBuilder()
//                .setSigningKey(getSecretKey()) // No change here, but method is now on the builder
//                .build() // Build the JwtParser instance
//                .parseClaimsJws(token)
//                .getBody();
//    }

    /**
     * Checks if a given JWT token has expired.
     *
     * @param token The JWT token to be checked.
     * @return {@code true} if the token has expired, otherwise {@code false}.
     */
    public static boolean isTokenExpired(String token) {
        // Retrieve claims from the token
        Claims claims = null;
        try {
             claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e){
            return true;
        }
        // Retrieve the expiration date from the claims.
        Date expiration = claims.getExpiration();
        // Compare the expiration date with the current date.
        return expiration.before(new Date());
    }


//    /**
//     * Parses a JWT token and deserializes the subject into a specific class type.
//     *
//     * @param token The JWT token to be parsed.
//     * @param clazz The class type to deserialize the subject into.
//     * @param <T>   The type of the class.
//     * @return An instance of {@code T} with fields populated from the token's subject.
//     */
//    public static <T> T parseJWT(String token, Class<T> clazz) {
//        Claims body = parseJWT(token);
//        try {
//            return JSONUtil.deserialize(body.getSubject(), clazz);
//        } catch (IOException e) {
//            log.error("Error deserializing token subject into class", e);
//            throw new RuntimeException("Token parsing error", e);
//        }
//    }

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

    public static boolean isTokenInvalid(String token){
        return isInBlacklist(token) || isTokenExpired(token);
    }

}