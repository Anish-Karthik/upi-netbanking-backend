// UPIRouter.java
package site.anish_karthik.upi_net_banking.server.controller.api.accounts.upi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.UpiService;
import site.anish_karthik.upi_net_banking.server.service.impl.UpiServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.util.List;

public class UPIRouter {
    private final UpiService upiService;
    private final String upiRegex;

    public UPIRouter() {
        try {
            upiService = new UpiServiceImpl();
            upiRegex = "\\S+";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public UPIRouter(UpiService upiService, String upiRegex) {
        this.upiService = upiService;
        this.upiRegex = upiRegex;
    }
    public UPIRouter(UpiService upiService) {
        this(upiService, "\\S+");
    }
    public UPIRouter(String upiRegex) throws Exception {
        this(new UpiServiceImpl(), upiRegex);
    }


    public void register(Router router) {
        router.get("/\\d+/upi/" + upiRegex, this::getUpiById);
        router.get("/\\d+/upi", this::getUpiByAccNo);
        router.post("/\\d+/upi", this::createUpi);
        router.put("/\\d+/upi/" + upiRegex + "/pin", this::updateUpiPin);
        router.put("/\\d+/upi/" + upiRegex + "/status", this::updateUpiStatus);
        router.put("/\\d+/upi/" + upiRegex + "/default", this::updateUpiDefault);
        router.delete("/\\d+/upi/" + upiRegex, this::deactivateUpi);
    }

    private void getUpiById(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String upiId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/upi/(" + upiRegex + ")", String.class);
            GetUpiDTO upi = upiService.getUpiById(upiId);
            upi.setAccNo(PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/upi/" + upiRegex, String.class));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI found", upi);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void getUpiByAccNo(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/upi", String.class);
            List<GetUpiDTO> upiList = upiService.getUpiByAccNo(accNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI found", upiList);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void createUpi(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CreateUpiDTO createUpiDTO = HttpRequestParser.parse(req, CreateUpiDTO.class);
            var res = upiService.createUpi(createUpiDTO);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "UPI created", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateUpiPin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateUpiPinDTO updateUpiPinDTO = HttpRequestParser.parse(req, UpdateUpiPinDTO.class);
            String upiId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/upi/(" + upiRegex + ")/pin", String.class);
            upiService.updateUpiPin(updateUpiPinDTO, upiId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI PIN updated", updateUpiPinDTO);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateUpiStatus(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateUpiDTOStatus updateUpiDTOStatus = HttpRequestParser.parse(req, UpdateUpiDTOStatus.class);
            String upiId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/upi/(" + upiRegex + ")/status", String.class);
            var res = UpdateUpiDTOStatus.fromUpi(upiService.updateUpiStatus(updateUpiDTOStatus, upiId));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI status updated", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateUpiDefault(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateUpiDTODefault updateUpiDTODefault = HttpRequestParser.parse(req, UpdateUpiDTODefault.class);
            String upiId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/upi/(" + upiRegex + ")/default", String.class);
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/upi/" + upiRegex + "/default", String.class);
            var res = UpdateUpiDTODefault.fromUpi(upiService.changeDefaultUpi(updateUpiDTODefault, upiId, accNo));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI default status updated", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void deactivateUpi(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/.*", String.class);
            String upiId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/upi/(" + upiRegex + ")", String.class);
            var res = upiService.deactivate(upiId, accNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI deactivated", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}