package site.anish_karthik.upi_net_banking.server.filter.validator;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.UserProfileDTO;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;

public class UserProfileFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String pathInfo = httpRequest.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+/profile")) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("HEY I'm Profile filter");

        if ("PUT".equals(method)) {
            validateUpdateUserProfileRequest(httpRequest, httpResponse, chain);
        } else {
            // handle get requests
            chain.doFilter(request, response);
        }
    }

    private void validateUpdateUserProfileRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        // Parse request body into UpdateUserDTO (similar approach as above)
        try {
            UserProfileDTO profileDTO = HttpRequestParser.parse(request, UserProfileDTO.class);
            if (profileDTO != null && isValidProfile(profileDTO) ) {
                chain.doFilter(request, response);  // Proceed to the servlet
            } else {
                ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid profile data", null);
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid profile data", null);
        }
    }

    private boolean isValidProfile(UserProfileDTO userDTO) {
        return userDTO.getName() != null && userDTO.getAddress() != null && userDTO.getDob() != null;
    }
}
