package com.embedcraft.embedcraftcore.httpfilter;

import com.embedcraft.embedcraftcore.util.JWTUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // Get the old token
        String oldToken = request.getHeader("Authorization");
        if (oldToken == null || oldToken.isEmpty()){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        Integer userId = JWTUtil.parseUserIdFromToken(oldToken);
        String newToken = JWTUtil.createJWT(userId);
        response.setHeader("Authorization", newToken);
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
