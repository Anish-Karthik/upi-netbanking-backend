package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;
import java.io.IOException;


public class AccountsAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        System.out.println("HEY I'm A accounts authorization filter::1");
        SessionUserDTO user = (SessionUserDTO) request.getAttribute("user");
        System.out.println("user: " + user);
        if (user == null) {
            ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. Please login.", null);
            return;
        }

        try {
            new AccountFilterModule().handle(httpRequest,httpResponse);
            chain.doFilter(request, response);
        } catch (ApiResponseException ae) {
            System.out.println("Error: " + ae.getApiResponse());
            ResponseUtil.sendResponse(httpRequest, httpResponse, ae.getApiResponse());
        } catch (Exception e) {
            ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
