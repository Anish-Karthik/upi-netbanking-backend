// UpiCardController.java
package site.anish_karthik.upi_net_banking.server.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.service.UpiService;
import site.anish_karthik.upi_net_banking.server.service.impl.CardServiceImpl;
import site.anish_karthik.upi_net_banking.server.service.impl.UpiServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/accounts/*")
public class UpiCardController extends HttpServlet {
    public static String upiRegex = "\\S+";
    private final UpiService upiService;
    private final CardService cardService;
    {
        try {
            upiService = new UpiServiceImpl();
            cardService = new CardServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Logger LOGGER = Logger.getLogger(UpiCardController.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        System.out.println(pathInfo);
        try {
            if (pathInfo.matches("/\\d+/upi.*")) {
                System.out.println("Get all upi path 1");
                if (pathInfo.matches("/\\d+/upi/"+upiRegex)) {
                    String upiId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/upi/("+upiRegex+")", String.class);
                    GetUpiDTO upi = upiService.getUpiById(upiId);
                    upi.setAccNo(PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/upi/"+upiRegex, String.class));
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI found", upi);
                } else {
                    System.out.println("Get all upi by acc no");
                    String accNo = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/upi", String.class);
                    List<GetUpiDTO> upiList = upiService.getUpiByAccNo(accNo);
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI found", upiList);
                }
            } else if (pathInfo.matches("/\\d+/card.*")) {
                if (pathInfo.matches("/\\d+/card/\\d+")) {
                    String cardNo = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/card/(\\d+)", String.class);
                    GetCardDTO card = cardService.getCardByCardNo(cardNo);
                    card.setAccNo(PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/card/\\d+", String.class));
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card found", card);
                } else {
                    String accNo = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/card", String.class);
                    List<GetCardDTO> cardList = cardService.getCardByAccNo(accNo);
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card found", cardList);
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
            if (pathInfo.matches("/\\d+/upi")) {
                CreateUpiDTO createUpiDTO = HttpRequestParser.parse(req, CreateUpiDTO.class);
                var res = upiService.createUpi(createUpiDTO);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "UPI created", res);
            } else if (pathInfo.matches("/\\d+/card")) {
                CreateCardDTO createCardDTO = HttpRequestParser.parse(req, CreateCardDTO.class);
                var res = cardService.createCard(createCardDTO);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Card created", res);
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
        if (pathInfo.matches("/\\d+/upi/"+upiRegex+"/pin")) {
            UpdateUpiPinDTO updateUpiPinDTO = HttpRequestParser.parse(req, UpdateUpiPinDTO.class);
            String upiId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/upi/("+upiRegex+")/pin", String.class);
            upiService.updateUpiPin(updateUpiPinDTO, upiId);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI PIN updated", updateUpiPinDTO);
        } else if (pathInfo.matches("/\\d+/card/\\d+/pin")) {
            UpdateCardPinDTO updateCardPinDTO = HttpRequestParser.parse(req, UpdateCardPinDTO.class);
            var cardNo = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/card/(\\d+)/pin", String.class);
            cardService.updateCardPin(updateCardPinDTO, cardNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card PIN updated", updateCardPinDTO);
        } else if (pathInfo.matches("/\\d+/upi/"+upiRegex)) {
            UpdateUpiDTO updateUpiDTO = HttpRequestParser.parse(req, UpdateUpiDTO.class);
            String upiId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/upi/("+upiRegex+")", String.class);
            var res = UpdateUpiDTO.fromUpi(upiService.updateUpi(updateUpiDTO, upiId));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI updated", res);
        } else if (pathInfo.matches("/\\d+/card/\\d+")) {
            UpdateCardDTO updateCardDTO = HttpRequestParser.parse(req, UpdateCardDTO.class);
            String cardNo = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/card/(\\d+)", String.class);
            var res = UpdateCardDTO.fromCard(cardService.updateCard(updateCardDTO, cardNo));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card updated", res);
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
            if (pathInfo.matches("/\\d+/upi/"+upiRegex)) {
                String upiId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/upi/("+upiRegex+")", String.class);
                var res = upiService.deactivateUpi(upiId);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "UPI deactivated", res);
            } else if (pathInfo.matches("/\\d+/card/\\d+")) {
                String cardId = PathParamExtractor.extractPathParams(pathInfo, "/\\d+/card/(\\d+)", String.class);
                var res = cardService.deactivateCard(cardId);
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card deactivated", res);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doDelete", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}