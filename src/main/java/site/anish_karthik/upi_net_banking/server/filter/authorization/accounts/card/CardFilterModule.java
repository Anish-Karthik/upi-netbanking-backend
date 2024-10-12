package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.card;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.CardDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateCardDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class CardFilterModule extends BaseFilterModule implements FilterModule {
    private final CardDao cardDao;
    private String path;
    private SessionUserDTO user;

    public CardFilterModule() {
        cardDao = new CardDaoImpl();
        registerCommonFilter("/\\d+/card/\\d+.*", this::commonFilter);
        registerMethodFilter("POST", "/\\d+/card/?", this::createCardFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");

        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A card authorization filter");
            String cardNo = PathParamExtractor.extractPathParams(path, "/\\d+/card/(\\d+).*", String.class);
            Optional<Card> card = cardDao.findById(cardNo);
            if (card.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Card not found");
            } else if (!card.get().getAccNo().equals(PathParamExtractor.extractPathParams(path, "/(\\d+)/card/\\d+.*", String.class))) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createCardFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A Create Card authorization filter");
            CreateCardDTO createCardDTO = HttpRequestParser.parse(httpRequest, CreateCardDTO.class);
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/card.*", String.class);
            authenticatePostRequest(accNo, user, createCardDTO.getAccNo(), createCardDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}