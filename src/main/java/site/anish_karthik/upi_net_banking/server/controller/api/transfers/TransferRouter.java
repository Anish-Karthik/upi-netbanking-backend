package site.anish_karthik.upi_net_banking.server.controller.api.transfers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.TransferService;
import site.anish_karthik.upi_net_banking.server.service.impl.TransferServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TransferRouter {
    private final TransferService transferService;
    public TransferRouter() {
        try {
            this.transferService = new TransferServiceImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Router router) {
        router.post("/", this::makeTransfer);
        router.get("/", this::getTransfers);
        router.get("/:refId", this::getTransfer);
    }

    private void makeTransfer(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("JO");
            CreateTransferDTO createTransferDTO = HttpRequestParser.parse(req, CreateTransferDTO.class);
            SessionUserDTO sessionUserDTO = (SessionUserDTO) req.getAttribute("user");
            createTransferDTO.getPayerTransaction().setUserId(sessionUserDTO.getId());
            GetTransferDTO res = transferService.handleTransfer(createTransferDTO);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transfer successful", res);
        } catch (IOException e) {
            handleException(req, resp, e);
        }
    }

    private void getTransfer(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String refId = req.getPathInfo().substring(1);
            GetTransferDTO res = transferService.getTransfer(refId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transfer found", res);
        } catch (IOException e) {
            handleException(req, resp, e);
        }
    }

    private void getTransfers(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<BankTransfer> res = transferService.getTransfers();
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Transfers found", res);
        } catch (IOException e) {
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
