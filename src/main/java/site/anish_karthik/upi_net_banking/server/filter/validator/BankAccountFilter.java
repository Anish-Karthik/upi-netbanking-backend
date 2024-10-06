package site.anish_karthik.upi_net_banking.server.filter.validator;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.CreateBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.utils.CachedBodyHttpServletRequest;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;
import site.anish_karthik.upi_net_banking.server.utils.validator.Validator;
import site.anish_karthik.upi_net_banking.server.utils.validator.ValidatorBuilder;

import java.io.IOException;

//@WebFilter(urlPatterns = {"/api/user/*"})
public class BankAccountFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String pathInfo = httpRequest.getPathInfo();

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
        System.out.println("HEY I'm A filter: BankAccountFilter");
        if (pathInfo != null && pathInfo.matches("/\\d+/accounts/\\d+/\\S+") &&pathInfo.endsWith("/reopen") || pathInfo.endsWith("/close")) {
            chain.doFilter(request, response);
        } else if (pathInfo.matches("/\\d+/accounts/?(\\d+)?")) {
            if ("POST".equals(method)) {
                validateCreateBankAccountRequest(cachedRequest, httpResponse, chain);
            } else if ("PUT".equals(method)) {
                validateUpdateBankAccountRequest(cachedRequest, httpResponse, chain);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void validateCreateBankAccountRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        try {
            CreateBankAccountDTO createDTO = HttpRequestParser.parse(request, CreateBankAccountDTO.class);
            if (createDTO != null && isValidCreateBankAccount(createDTO)) {
                if (handleValidation(createDTO, request, response, chain)) {
                    chain.doFilter(request, response);
                }
            } else {
                ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid bank account data", null);
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid bank account data", null);
        }
    }

    private void validateUpdateBankAccountRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        try {
            UpdateBankAccountDTO updateDTO = HttpRequestParser.parse(request, UpdateBankAccountDTO.class);
            if (updateDTO != null && isValidUpdateBankAccount(updateDTO)) {
                chain.doFilter(request, response);
            } else {
                ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid bank account data", null);
            }
        } catch (Exception e) {
            ResponseUtil.sendResponse(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid bank account data", null);
        }
    }

    private boolean isValidCreateBankAccount(CreateBankAccountDTO dto) {
        return dto.getAccNo() != null && dto.getIfsc() != null && dto.getBankId() != null;
    }

    private boolean isValidUpdateBankAccount(UpdateBankAccountDTO dto) {
        return dto.getAccountType() != null && dto.getStatus() != null;
    }

    private Boolean handleValidation(CreateBankAccountDTO dto,HttpServletRequest req, HttpServletResponse response, FilterChain chain) throws IOException {
        Validator<String> accNoValidator = new ValidatorBuilder<String>()
                .addNotNull("Account number cannot be null")
                .addRegex("^[0-9]+$", "Invalid account number")
                .addSize(10, 55, "Invalid account number")
                .build();
        Validator<String> ifscValidator = new ValidatorBuilder<String>()
                .addNotNull("IFSC code cannot be null")
                .addSize(11, 20, "IFSC code must be between 11 and 20 characters")
                .build();
        Validator<Long> bankIdValidator = new ValidatorBuilder<Long>()
                .addNotNull("Bank ID cannot be null")
                .build();
        try {
            accNoValidator.validate(dto.getAccNo());
            ifscValidator.validate(dto.getIfsc());
            bankIdValidator.validate(dto.getBankId());
        } catch (Exception e) {
            ResponseUtil.sendResponse(req, response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
            return false;
        }
        return true;
    }
}