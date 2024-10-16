package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.transaction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransactionDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransactionDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class TransactionFilterModule extends BaseFilterModule implements FilterModule {
    private final TransactionDao transactionDao;
    private String path;
    private SessionUserDTO user;

    public TransactionFilterModule() {
        transactionDao = new TransactionDaoImpl();
        registerCommonFilter("/\\d+/transactions/\\d+.*", this::commonFilter);
        registerMethodFilter("POST", "/\\d+/transactions/?", this::createTransactionFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("HEY I'm A transactions authorization filter::2");
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");

        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A transactions authorization filter");
            Long txnId = Long.parseLong(PathParamExtractor.extractPathParams(path, "/\\d+/transactions/(\\d+).*", String.class));
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/transactions/\\d+.*", String.class);
            Optional<Transaction> transaction = transactionDao.findById(txnId);
            if (transaction.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Transaction not found");
            } else if (!transaction.get().getAccNo().equals(accNo)) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createTransactionFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A Create Transaction authorization filter");
            CreateTransactionDTO createTransactionDTO = HttpRequestParser.parse(httpRequest, CreateTransactionDTO.class);
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/transactions.*", String.class);
            authenticatePostRequest(accNo, user, createTransactionDTO.getAccNo(), createTransactionDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}