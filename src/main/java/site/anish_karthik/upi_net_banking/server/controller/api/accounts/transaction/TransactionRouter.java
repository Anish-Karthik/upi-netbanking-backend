package site.anish_karthik.upi_net_banking.server.controller.api.accounts.transaction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransactionDTO;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.service.impl.TransactionServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.util.List;

public class TransactionRouter {
    private final TransactionService transactionService = new TransactionServiceImpl();

    public void register(Router router) {
        router.get("/:accNo/transactions", this::getTransactions);
        router.get("/:accNo/transactions/:transactionId", this::getTransactionById);
        router.post("/:accNo/transactions", this::processTransaction);
    }

    public void getTransactions(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(),"/(\\d+)/transactions", String.class);
            List<Transaction> transactions = transactionService.getTransactionsByAccNo(accNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transactions Found", transactions);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    public void getTransactionById(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("GET TRANSACTION BY ID");
        try {
            String path = req.getPathInfo();
            String accNo = PathParamExtractor.extractPathParams(path,"/(\\d+)/transactions.*", String.class);
            Long transactionId = Long.parseLong(PathParamExtractor.extractPathParams(path, "/\\d+/transactions/(\\d+).*", String.class));
            System.out.println(accNo + " " + transactionId);
            Transaction transaction = transactionService.getTransactionById(transactionId);
            System.out.println(transaction);
            if (!transaction.getAccNo().equals(accNo)) {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_NOT_FOUND, "Transaction not found", null);
            }
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transaction Found", transaction);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    public void processTransaction(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CreateTransactionDTO transactionDTO = HttpRequestParser.parse(req, CreateTransactionDTO.class);
            Transaction transaction = transactionService.handleTransaction(transactionDTO.toTransaction(), TransactionCategory.SOLO);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transaction Success", transaction);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @Data
    public class PathParams {
        private String accNo;
        private Long transactionId;
    }
}
