package site.anish_karthik.upi_net_banking.server.controller.api.bankaccounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/accounts/*") // RESTful endpoint
public class BankAccountController extends HttpServlet {

    private final BankAccountService bankAccountService;

    {
        try {
            bankAccountService = new BankAccountServiceImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper for JSON serialization

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            // Extract the account number from URL
            String accNo = pathInfo.substring(1);
            BankAccount account = bankAccountService.getBankAccountByAccNo(accNo);
            if (account != null) {
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), account);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
            }
        } else {
            // If no specific account, retrieve by userId
            String userIdParam = req.getParameter("userId");
            if (userIdParam != null) {
                long userId = Long.parseLong(userIdParam);
                List<BankAccount> accounts = bankAccountService.getBankAccountsByUserId(userId);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), accounts);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId parameter");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankAccount account = objectMapper.readValue(req.getInputStream(), BankAccount.class);
        BankAccount result = bankAccountService.addBankAccount(account);
        if (result != null) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getOutputStream(), account);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create account");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            // Extract the account number from URL
            String accNo = pathInfo.substring(1);
            BankAccount account = objectMapper.readValue(req.getInputStream(), BankAccount.class);
            account.setAccNo(accNo);

            BankAccount result = bankAccountService.updateBankAccount(account);
            if (result != null) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getOutputStream(), account);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update account");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number is required for updating");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            // Extract the account number from URL
            String accNo = pathInfo.substring(1);

            bankAccountService.deleteBankAccount(accNo);
            if (true) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found or could not be deleted");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number is required for deletion");
        }
    }
}