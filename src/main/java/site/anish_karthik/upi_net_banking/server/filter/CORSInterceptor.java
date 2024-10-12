package site.anish_karthik.upi_net_banking.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.utils.CachedBodyHttpServletRequest;

import java.io.IOException;

public class CORSInterceptor implements Filter {

    private static final String[] allowedOrigins = {
            "http://localhost:3000", "http://localhost:5500", "http://localhost:5501", "http://localhost:5173", "http://127.0.0.1:5500"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("HEY I'm A filter: CORSInterceptor");
        String requestOrigin = request.getHeader("Origin");
        if (isAllowedOrigin(requestOrigin)) {
            response.setHeader("Access-Control-Allow-Origin", requestOrigin);
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.setHeader("Access-Control-Max-Age", "3600");

            // CORS handshake (pre-flight request)
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return;
            }
        }
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        // pass the request along the filter chain
        filterChain.doFilter(cachedRequest, response);
    }

    private boolean isAllowedOrigin(String origin) {
        if (origin == null) {
            return false;
        }
        for (String allowedOrigin : allowedOrigins) {
            if (origin.equals(allowedOrigin)) {
                return true;
            }
        }
        return false;
    }
}