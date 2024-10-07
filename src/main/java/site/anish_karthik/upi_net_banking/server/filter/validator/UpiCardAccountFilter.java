package site.anish_karthik.upi_net_banking.server.filter.validator;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.AccountStatusDTO;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.CachedBodyHttpServletRequest;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.sql.SQLException;

public class UpiCardAccountFilter implements Filter {
    private final static BankAccountService accountService;
    static {
        try {
            accountService = new BankAccountServiceImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String pathInfo = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        if (pathInfo == null || !pathInfo.matches("/\\d+/accounts/\\d+/(upi|card)/?.*")) {
            chain.doFilter(request, response);
            return;
        }
        System.out.printf("HEY I'm A filter: UpiCardAccountFilter %s %s\n", method, pathInfo);
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
        try {
            String accNo = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/.*", String.class);
            if (pathInfo.matches("/\\d+/\\S+")) {
                String paymentMode = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/(\\S+)/*", String.class);
                System.out.printf("HEY I'm A filter: UpiCardAccountFilter %s %s\n",accNo, paymentMode);
                AccountStatusDTO accountStatusDTO = AccountStatusDTO.fromBankAccount(accountService.getBankAccountByAccNo(accNo));
                if (accountStatusDTO != null && !method.equals("GET") && "CLOSED".equalsIgnoreCase(accountStatusDTO.getStatus().name())) {
                    ResponseUtil.sendResponse(cachedRequest, httpResponse, HttpServletResponse.SC_BAD_REQUEST, "Account is closed, hence won't be able to manipulate " + paymentMode + " or other supported payment methods linked with account", null);
                } else {
                    chain.doFilter(request, response);
                }
            }  else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_BAD_REQUEST, "Error processing account status", null);
        }
    }
}