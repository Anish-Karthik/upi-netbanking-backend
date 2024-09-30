package site.anish_karthik.upi_net_banking.server.controller.api.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.service.BeneficiaryService;
import site.anish_karthik.upi_net_banking.server.service.impl.BeneficiaryServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/users/*")
public class BeneficiaryController extends HttpServlet {
    private final BeneficiaryService beneficiaryService;
    {
        try {
            beneficiaryService = new BeneficiaryServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Logger LOGGER = Logger.getLogger(BeneficiaryController.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo.matches("/\\d+/beneficiaries.*")) {
                if (pathInfo.matches("/\\d+/beneficiaries/\\d+")) {
                    String beneficiaryId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/beneficiaries/(\\d+)", String.class);
                    Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(beneficiaryId);
                    System.out.printf("Beneficiary: %s\n", beneficiary);
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary found", beneficiary);
                } else {
                    String userId = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/beneficiaries", String.class);
                    List<Beneficiary> beneficiaryList = beneficiaryService.getBeneficiariesByUserId(userId);
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiaries found", beneficiaryList);
                }
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doGet", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo.matches("/\\d+/beneficiaries")) {
                CreateBeneficiaryDTO createBeneficiaryDTO = HttpRequestParser.parse(req, CreateBeneficiaryDTO.class);
                var res = beneficiaryService.createBeneficiary(createBeneficiaryDTO);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Beneficiary created", res);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path", null);
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
            if (pathInfo.matches("/\\d+/beneficiaries/\\d+")) {
                UpdateBeneficiaryDTO updateBeneficiaryDTO = HttpRequestParser.parse(req, UpdateBeneficiaryDTO.class);
                String beneficiaryId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/beneficiaries/(\\d+)", String.class);
                var res = UpdateBeneficiaryDTO.fromBeneficiary(beneficiaryService.updateBeneficiary(updateBeneficiaryDTO, beneficiaryId));
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary updated", res);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path", null);
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
            if (pathInfo.matches("/\\d+/beneficiaries/\\d+")) {
                String beneficiaryId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/beneficiaries/(\\d+)", String.class);
                var res = beneficiaryService.deleteBeneficiary(beneficiaryId);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Beneficiary deactivated", res);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doDelete", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}