package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.card.CardFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.transaction.TransactionFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.upi.UPIFilterModule;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.sql.SQLException;
import java.util.Optional;

public class AccountFilterModule extends BaseFilterModule implements FilterModule {
    private final BankAccountDao bankAccountDao;
    private String path;

    public AccountFilterModule() {
        try {
            bankAccountDao = new BankAccountDaoImpl(DatabaseUtil.getConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        registerNestedFilter("/\\d+/upi.*", new UPIFilterModule());
        registerNestedFilter("/\\d+/card.*", new CardFilterModule());
        registerNestedFilter("/\\d+/transactions.*", new TransactionFilterModule());

        registerCommonFilter("/(\\d+).*", this::commonFilter);
    }
    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("HEY I'm A accounts authorization filter::2");
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();

        applyFilters(method, path, httpRequest, httpResponse);
        applyNestedFilters(httpRequest, httpResponse, path);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            SessionUserDTO user = (SessionUserDTO) httpRequest.getAttribute("user");
            System.out.println("HEY I'm A accounts authorization filter");
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+).*", String.class);
            Optional<BankAccount> bankAccount = bankAccountDao.findById(accNo);
            if (bankAccount.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Bank Account not found");
            } else if (!bankAccount.get().getUserId().equals(user.getId())) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
            System.out.println("HEY I'm A accounts authorization filter:: success");
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
