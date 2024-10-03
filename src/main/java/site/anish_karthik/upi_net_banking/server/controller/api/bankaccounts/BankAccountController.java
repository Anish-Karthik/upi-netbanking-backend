package site.anish_karthik.upi_net_banking.server.controller.api.bankaccounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.dto.CreateBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/user/*") // RESTful endpoint
public class BankAccountController extends HttpServlet {

    private final BankAccountService bankAccountService;

    {
        try {
            bankAccountService = new BankAccountServiceImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Data
    public static class UserAccountParams {
        private Long userId;
        private String accNo;
    }
    Logger LOGGER = Logger.getLogger(BankAccountController.class.getName());
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        /api/users/userId/accounts
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/?(\\d+)?", UserAccountParams.class);

            if (params.getUserId() != null && params.getAccNo() != null) {
                // Extract the account number from URL
                GetBankAccountDTO account = bankAccountService.getBankAccountWithBankByAccNo(params.getAccNo());
                if (account != null) {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account found", account);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_NOT_FOUND, "Account not found", null);
                }
            } else if (params.getUserId() != null) {
                System.out.println("I'm in doGet: " + params.getUserId());
                // If no specific account, retrieve by userId
                List<GetBankAccountDTO> accounts = bankAccountService.getBankAccountsWithBankByUserId(params.getUserId());
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Accounts found", accounts);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error in doGet", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("I'm in doPost: Account");
        BankAccount account = HttpRequestParser.parse(req, CreateBankAccountDTO.class).toBankAccount();
        try {
            var params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/accounts", UserAccountParams.class);
            if (params.getUserId() != null) {
                System.out.println("I'm in doPost: " + params.getUserId());
                account.setUserId(params.getUserId());
                var result = CreateBankAccountDTO.fromBankAccount(bankAccountService.addBankAccount(account));
                if (result != null) {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Account created", result);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Account could not be created", null);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPost", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/(\\d+)", UserAccountParams.class);
            if (params.getUserId() != null && params.getAccNo() != null) {
                BankAccount account = HttpRequestParser.parse(req, UpdateBankAccountDTO.class).toBankAccount();
//                account.setUserId(params.getUserId());
                account.setAccNo(params.getAccNo());
                System.out.println("I'm in doPut: " + params.getUserId() + account);
                var updatedAccount = UpdateBankAccountDTO.fromBankAccount(bankAccountService.updateBankAccount(account));
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account updated", updatedAccount);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID and Account number are required for updating", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPut", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/(\\d+)", UserAccountParams.class);
            if (params.getUserId() != null && params.getAccNo() != null) {
                bankAccountService.deleteBankAccount(params.getAccNo());
                var account = bankAccountService.getBankAccountWithBankByAccNo(params.getAccNo());
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account closed", account);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID and Account number are required for deletion", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doDelete", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}