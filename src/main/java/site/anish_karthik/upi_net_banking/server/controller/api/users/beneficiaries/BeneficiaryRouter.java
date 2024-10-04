package site.anish_karthik.upi_net_banking.server.controller.api.users.beneficiaries;

import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.BeneficiaryService;
import site.anish_karthik.upi_net_banking.server.service.impl.BeneficiaryServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;
import site.anish_karthik.upi_net_banking.server.dto.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeneficiaryRouter {
    private final BeneficiaryService beneficiaryService;
    private final Logger LOGGER = Logger.getLogger(BeneficiaryRouter.class.getName());

    public BeneficiaryRouter() {
        try {
            beneficiaryService = new BeneficiaryServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Router router) {
        router.get("/\\d+/beneficiaries/\\d+", this::getBeneficiaryById);
        router.get("/\\d+/beneficiaries", this::getBeneficiariesByUserId);
        router.post("/\\d+/beneficiaries", this::createBeneficiary);
        router.put("/\\d+/beneficiaries/\\d+", this::updateBeneficiary);
        router.delete("/\\d+/beneficiaries/\\d+", this::deleteBeneficiary);
    }

    private void getBeneficiaryById(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String beneficiaryId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/beneficiaries/(\\d+)", String.class);
            Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(beneficiaryId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary found", beneficiary);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void getBeneficiariesByUserId(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String userId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/beneficiaries", String.class);
            List<Beneficiary> beneficiaryList = beneficiaryService.getBeneficiariesByUserId(userId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiaries found", beneficiaryList);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void createBeneficiary(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CreateBeneficiaryDTO createBeneficiaryDTO = HttpRequestParser.parse(req, CreateBeneficiaryDTO.class);
            var res = beneficiaryService.createBeneficiary(createBeneficiaryDTO);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Beneficiary created", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateBeneficiary(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateBeneficiaryDTO updateBeneficiaryDTO = HttpRequestParser.parse(req, UpdateBeneficiaryDTO.class);
            String beneficiaryId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/beneficiaries/(\\d+)", String.class);
            var res = UpdateBeneficiaryDTO.fromBeneficiary(beneficiaryService.updateBeneficiary(updateBeneficiaryDTO, beneficiaryId));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary updated", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void deleteBeneficiary(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String beneficiaryId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/beneficiaries/(\\d+)", String.class);
            var res = beneficiaryService.deleteBeneficiary(beneficiaryId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary deactivated", res);
        } catch (Exception e) {
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
}
