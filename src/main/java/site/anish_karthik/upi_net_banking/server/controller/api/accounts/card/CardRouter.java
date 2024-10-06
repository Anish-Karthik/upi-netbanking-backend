package site.anish_karthik.upi_net_banking.server.controller.api.accounts.card;

import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.service.impl.CardServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;
import site.anish_karthik.upi_net_banking.server.dto.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CardRouter {
    private static CardRouter instance;
    private final CardService cardService;

    {
        try {
            cardService = new CardServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized CardRouter getInstance() {
        if (instance == null) {
            instance = new CardRouter();
        }
        return instance;
    }

    public void register(Router router) {
        router.get("/\\d+/card/\\d+", (req, resp) -> getCardByCardNo(req, resp));
        router.get("/\\d+/card", this::getCardByAccNo);
        router.post("/\\d+/card", this::createCard);
        router.put("/\\d+/card/\\d+/pin", this::updateCardPin);
        router.put("/\\d+/card/\\d+", this::updateCard);
        router.delete("/\\d+/card/\\d+", this::deactivateCard);
    }

    private void getCardByCardNo(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cardNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/card/(\\d+)", String.class);
            GetCardDTO card = cardService.getCardByCardNo(cardNo);
            card.setAccNo(PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/card/\\d+", String.class));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card found", card);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void getCardByAccNo(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/card", String.class);
            List<GetCardDTO> cardList = cardService.getCardByAccNo(accNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card found", cardList);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void createCard(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CreateCardDTO createCardDTO = HttpRequestParser.parse(req, CreateCardDTO.class);
            var res = cardService.createCard(createCardDTO);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_CREATED, "Card created", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateCardPin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateCardPinDTO updateCardPinDTO = HttpRequestParser.parse(req, UpdateCardPinDTO.class);
            var cardNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/card/(\\d+)/pin", String.class);
            cardService.updateCardPin(updateCardPinDTO, cardNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card PIN updated", updateCardPinDTO);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void updateCard(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UpdateCardDTO updateCardDTO = HttpRequestParser.parse(req, UpdateCardDTO.class);
            String cardNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/card/(\\d+)", String.class);
            var res = UpdateCardDTO.fromCard(cardService.updateCard(updateCardDTO, cardNo));
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card updated", res);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void deactivateCard(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String accNo = PathParamExtractor.extractPathParams(req.getPathInfo(), "/(\\d+)/.*", String.class);
            String cardId = PathParamExtractor.extractPathParams(req.getPathInfo(), "/\\d+/card/(\\d+)", String.class);
            var res = cardService.deactivate(cardId, accNo);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Card deactivated", res);
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