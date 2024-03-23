package com.embedcraft.embedcraftcore.httpfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A filter that adds Cross-Origin Resource Sharing (CORS) headers to the response.
 * This allows client applications hosted on different origins to interact with the API.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    /**
     * Constructor for CORSFilter.
     */
    public CORSFilter() {
    }

    /**
     * Applies CORS policies to any incoming HTTP request.
     *
     * @param req   The incoming servlet request.
     * @param res   The outgoing servlet response.
     * @param chain The filter chain to which the request and response should be passed if appropriate.
     * @throws IOException      Thrown if an I/O error occurs during the processing of the request.
     * @throws ServletException Thrown if the request cannot be handled.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // Cast ServletRequest and ServletResponse to their HTTP counterparts
        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;
        // Allow all origins
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Permit browsers to request methods POST, PUT, GET, OPTIONS, DELETE
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        // Allow browsers to send these headers
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        // Set cache duration for preflight response
        response.setHeader("Access-Control-Max-Age", "3600");
        // If it's a preflight request, respond with OK status and do not forward the request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // For all other request types, proceed with the rest of the filter chain
            chain.doFilter(req, res);
        }
    }

    /**
     * Initializes the filter with the given filter configuration. This method
     * is empty as this filter does not require any initialization logic.
     *
     * @param filterConfig The filter configuration.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Destroys the filter. This method is empty because the filter does not
     * require any cleanup logic upon destruction.
     */
    @Override
    public void destroy() {
    }
}
