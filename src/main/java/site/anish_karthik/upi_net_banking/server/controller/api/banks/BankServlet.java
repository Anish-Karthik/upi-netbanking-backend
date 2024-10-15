package site.anish_karthik.upi_net_banking.server.controller.api.banks;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.Bank;
import site.anish_karthik.upi_net_banking.server.service.BankService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BankServlet extends HttpServlet {

    private final BankService bankService;
    {
        try {
            bankService = new BankServiceImpl(new BankDaoImpl(DatabaseUtil.getConnection()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            long id = Long.parseLong(pathInfo.substring(1));
            Optional<Bank> bank = bankService.getBankById(id);
            if (bank.isPresent()) {
                resp.setContentType("application/json");
                HttpRequestParser.writeJson(resp, 200, "Bank found", bank);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Bank not found");
            }
        } else {
            List<Bank> banks = bankService.getAllBanks();
            resp.setContentType("application/json");
            HttpRequestParser.writeJson(resp, 200, "Banks found", banks);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bank bank = HttpRequestParser.parse(req, Bank.class);
        Bank createdBank = bankService.createBank(bank);
        resp.setContentType("application/json");
        HttpRequestParser.writeJson(resp, 201, "Bank created", createdBank);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            Bank bank = HttpRequestParser.parse(req, Bank.class);
            Bank updatedBank = bankService.updateBank(bank);
            resp.setContentType("application/json");
            HttpRequestParser.writeJson(resp, 200, "Bank updated", updatedBank);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bank id is required for updating");
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            long id = Long.parseLong(pathInfo.substring(1));
            Optional<Bank> bank = bankService.getBankById(id);
            if (bank.isEmpty()) {
                HttpRequestParser.writeJson(resp, 404, "Bank id not found");
                return;
            }
            bankService.deleteBank(id);
            resp.setContentType("application/json");
            HttpRequestParser.writeJson(resp, 200, "Bank deleted", bank);
        } else {
            HttpRequestParser.writeJson(resp, 400, "Bank id is required for deletion");
        }
    }
}
