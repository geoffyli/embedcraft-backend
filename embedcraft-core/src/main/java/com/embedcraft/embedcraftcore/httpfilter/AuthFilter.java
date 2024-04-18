package com.embedcraft.embedcraftcore.httpfilter;

import com.embedcraft.embedcraftcore.util.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@WebFilter(urlPatterns = "/*", filterName = "authFilter")
public class AuthFilter implements Filter {

    /*
    Define allowed paths.
     */
    private static final Set<String> ALLOWED_PATHS = Set.of("/login", "/signup", "/logout", "/model/upload", "/model/train", "/model/status", "/model/evaluation");

    /**
     * Implement doFilter method the check authorization.
     * @param servletRequest the servlet request
     * @param servletResponse the servlet response
     * @param filterChain the filter chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // Get the request path
        String path = request.getServletPath();
        // Skip filter for allowed paths
        if (ALLOWED_PATHS.contains(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // Get the token.
        String authToken = request.getHeader("Authorization");
        // If the token is null or empty, or the token is in the blacklist, set HTTP 401 status.
        if (authToken == null || authToken.isEmpty() || JWTUtil.isInBlacklist(authToken)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        // Check if the token is valid.
        try {
            // Validate token
            if (JWTUtil.isTokenExpired(authToken)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            // Token is valid, continue the request
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            // For any parsing error or invalid token, return HTTP unauthorized status
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}


