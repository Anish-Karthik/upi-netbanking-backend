package site.anish_karthik.upi_net_banking.server.controller.api.users.accounts;

import lombok.Data;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountRouter {
    private final BankAccountService bankAccountService;
    private static final Logger LOGGER = Logger.getLogger(AccountRouter.class.getName());

    public AccountRouter() {
        try {
            bankAccountService = new BankAccountServiceImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Router router) {
        router.get("/\\d+/accounts/?(\\d+)?", this::getBankAccount);
        router.post("/\\d+/accounts/?(\\d+)?", this::createOrUpdateBankAccount);
        router.put("/\\d+/accounts/(\\d+)", this::updateBankAccount);
        router.delete("/\\d+/accounts/(\\d+)", this::deleteBankAccount);
        router.post("/\\d+/accounts/\\d+/reopen", this::reopenBankAccount);
        router.post("/\\d+/accounts/\\d+/close", this::closeBankAccount);
        router.post("/\\d+/accounts/\\d+/status", this::updateAccountStatus);
    }

    private void getBankAccount(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/?(\\d+)?", UserAccountParams.class);

            if (params.getUserId() != null && params.getAccNo() != null) {
                GetBankAccountDTO account = bankAccountService.getBankAccountWithBankByAccNo(params.getAccNo());
                if (account != null) {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account found", account);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_NOT_FOUND, "Account not found", null);
                }
            } else if (params.getUserId() != null) {
                List<GetBankAccountDTO> accounts = bankAccountService.getBankAccountsWithBankByUserId(params.getUserId());
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Accounts found", accounts);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void createOrUpdateBankAccount(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/?(\\d+)?", UserAccountParams.class);

            if (params.getUserId() != null) {
                BankAccount account = HttpRequestParser.parse(req, CreateBankAccountDTO.class).toBankAccount();
                account.setUserId(params.getUserId());
                var result = CreateBankAccountDTO.fromBankAccount(bankAccountService.addBankAccount(account));
                if (result != null) {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Account created", result);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Account could not be created", null);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in createOrUpdateBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void updateBankAccount(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            var params = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/accounts/(\\d+)", UserAccountParams.class);
            if (params.getUserId() != null && params.getAccNo() != null) {
                BankAccount account = HttpRequestParser.parse(req, UpdateBankAccountDTO.class).toBankAccount();
                account.setAccNo(params.getAccNo());
                var updatedAccount = UpdateBankAccountDTO.fromBankAccount(bankAccountService.updateBankAccount(account));
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account updated", updatedAccount);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID and Account number are required for updating", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void deleteBankAccount(HttpServletRequest req, HttpServletResponse resp) {
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
            LOGGER.log(Level.SEVERE, "Error in deleteBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void reopenBankAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/accounts/(\\d+)/reopen", UserAccountParams.class);
            bankAccountService.reopenBankAccount(params.getAccNo());
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account reopened", null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in reopenBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void closeBankAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/accounts/(\\d+)/close", UserAccountParams.class);
            bankAccountService.closeBankAccount(params.getAccNo());
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account closed", null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in closeBankAccount", e);
            handleException(req, resp, e);
        }
    }

    private void updateAccountStatus(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/accounts/(\\d+)/status", UserAccountParams.class);
            AccountStatusDTO statusDTO = HttpRequestParser.parse(req, AccountStatusDTO.class);
            BankAccount account = statusDTO.toBankAccount();
            account.setAccNo(params.getAccNo());
            statusDTO = AccountStatusDTO.fromBankAccount(bankAccountService.updateBankAccount(account));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Account status updated", statusDTO);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateAccountStatus", e);
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error handling exception", ex);
        }
    }

    @Data
    public static class UserAccountParams {
        private Long userId;
        private String accNo;
    }
}