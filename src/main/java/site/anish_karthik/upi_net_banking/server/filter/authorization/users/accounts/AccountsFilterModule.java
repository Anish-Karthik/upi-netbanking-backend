package site.anish_karthik.upi_net_banking.server.filter.authorization.users.accounts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class AccountsFilterModule extends BaseFilterModule implements FilterModule {
    private final BankAccountDao accountDao;
    private String path;
    private SessionUserDTO user;

    public AccountsFilterModule() {
        accountDao = new BankAccountDaoImpl();
        registerCommonFilter("/\\d+/accounts/\\d+.*", this::commonFilter);
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
            Long userId = Long.parseLong(PathParamExtractor.extractPathParams(path, "/(\\d+)/accounts/\\d+.*", String.class));
            String accNo = PathParamExtractor.extractPathParams(path, "/\\d+/accounts/(\\d+).*", String.class);
            Optional<BankAccount> account = accountDao.findById(accNo);
            if (account.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Account not found");
            } else if (!account.get().getUserId().equals(userId)) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            } else if (!account.get().getUserId().equals(user.getId())) {
                throw new ApiResponseException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}