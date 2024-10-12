package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.card;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.CardDaoImpl;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.sql.SQLException;
import java.util.Optional;

public class CardFilterModule extends BaseFilterModule implements FilterModule {
    private final CardDao cardDao;
    private String path;

    public CardFilterModule() {
        try {
            cardDao = new CardDaoImpl(DatabaseUtil.getConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        registerCommonFilter("/\\d+/card/\\d+.*", this::commonFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();

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
}