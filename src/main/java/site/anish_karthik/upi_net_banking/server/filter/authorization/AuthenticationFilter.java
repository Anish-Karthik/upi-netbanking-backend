package site.anish_karthik.upi_net_banking.server.filter.authorization;

import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

//@WebFilter(urlPatterns = {"/api/*"})  // Apply the filter only to protected routes
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        System.out.println("HEY I'm A auth filter");
        // Check if there's a session
        HttpSession session = httpRequest.getSession(false);
//        httpRequest.setAttribute("user", new SessionUserDTO(1L, "anish", "anish@example.com", "1234567890"));
//        chain.doFilter(request, response);

        if (session != null && session.getAttribute("user") != null) {
//             User is authenticated
            System.out.println("User is authenticated");
            System.out.println(session.getAttribute("user"));
            SessionUserDTO user = (SessionUserDTO) session.getAttribute("user");
            httpRequest.setAttribute("user", user);
            chain.doFilter(request, response);
        } else {
            // Check if SESSIONID cookie is present
            Optional<Cookie> sessionCookie = getSessionCookie(httpRequest);

            if (sessionCookie.isPresent() && session != null) {
                // If session cookie exists, but session is invalid, invalidate it
                session.invalidate();
            }

            // Redirect to login page if the user is not authenticated
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized. Please login.");
        }
    }

    private Optional<Cookie> getSessionCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "SESSIONID".equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }
}
