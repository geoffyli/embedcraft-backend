package com.embedcraft.embedcraftcore.httpfilter;

import com.embedcraft.embedcraftcore.util.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A filter component that intercepts HTTP requests and responses in the application.
 * It checks for the presence of an authorization token, validates it,
 * and if valid, generates a new token and sets it in the response header.
 */
@Component
public class ResFilter implements Filter {

    /**
     * Filters incoming HTTP requests and manipulates the response.
     *
     * @param servletRequest  The incoming request object.
     * @param servletResponse The outgoing response object.
     * @param filterChain     The filter chain associated with this request, allowing further filters to process.
     * @throws IOException      If an I/O error occurs during the processing of the request.
     * @throws ServletException If the request could not be handled.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // Cast the ServletRequest and ServletResponse to their HTTP counterparts
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Attempt to retrieve the old token from the Authorization header
        String oldToken = request.getHeader("Authorization");

        // Proceed without modification if no token is found or the token is invalid
        if (oldToken == null || oldToken.isEmpty() || JWTUtil.isTokenInvalid(oldToken)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Parse the user ID from the old, valid token
        Integer userId = JWTUtil.parseUserIdFromToken(oldToken);
        // Create a new token for the user
        String newToken = JWTUtil.createJWT(userId);
        // Set the new token in the Authorization header of the response
        response.setHeader("Authorization", newToken);

        // Continue the filter chain
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
