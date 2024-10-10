package site.anish_karthik.upi_net_banking.server.filter.authorization;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.dao.impl.CardDaoImpl;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransactionDaoImpl;
import site.anish_karthik.upi_net_banking.server.dao.impl.UpiDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;


public class AccountsAuthFilter implements Filter {
    private BankAccountDao bankAccountDao;
    private UpiDao upiDao;
    private CardDao cardDao;
    private TransactionDao transactionDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        try {
            bankAccountDao = new BankAccountDaoImpl(DatabaseUtil.getConnection());
            upiDao = new UpiDaoImpl(DatabaseUtil.getConnection());
            cardDao = new CardDaoImpl(DatabaseUtil.getConnection());
            transactionDao = new TransactionDaoImpl(DatabaseUtil.getConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        System.out.println("HEY I'm A accounts authorization filter");
        SessionUserDTO user = (SessionUserDTO) ((HttpServletRequest) request).getAttribute("user");
        if (user == null) {
            ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. Please login.", null);
            return;
        }

        String path = httpRequest.getPathInfo();

        try {
            if (path.matches("/\\d+/.*")) {
                String accNo = PathParamExtractor.extractPathParams(path,"/(\\d+).*", String.class);
                Optional<BankAccount> bankAccount = bankAccountDao.findById(accNo);
                if (bankAccount.isEmpty()) {
                    ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_NOT_FOUND, "Account not found", null);
                    return;
                }
                if (!bankAccount.get().getUserId().equals(user.getId())) {
                    ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden", null);
                    return;
                }
                if (path.matches("/\\d+/upi/\\S+.*")) {
                    String upiId = PathParamExtractor.extractPathParams(path, "/\\d+/upi/(\\S+).*", String.class);
                    Optional<Upi> upi = upiDao.findById(upiId);

                    if (upi.isEmpty()) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_NOT_FOUND, "UPI not found", null);
                        return;
                    }
                    if (!upi.get().getAccNo().equals(accNo)) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden", null);
                        return;
                    }
                }
                if (path.matches("/\\d+/card/\\d+.*")) {
                    String cardNo = PathParamExtractor.extractPathParams(path, "/\\d+/card/(\\d+).*", String.class);
                    Optional<Card> card = cardDao.findById(cardNo);
                    if (card.isEmpty()) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_NOT_FOUND, "Card not found", null);
                        return;
                    }
                    if (!card.get().getAccNo().equals(accNo)) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden", null);
                        return;
                    }
                }

                if (path.matches("/\\d+/transactions/\\d+.*")) {
                    Long txnId = Long.parseLong(PathParamExtractor.extractPathParams(path, "/\\d+/transactions/(\\d+).*", String.class));
                    System.out.println("txnId: " + txnId);
                    Optional<Transaction> transaction = transactionDao.findById(txnId);
                    System.out.println("transaction: " + transaction);
                    if (transaction.isEmpty()) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_NOT_FOUND, "Transaction not found", null);
                        return;
                    }
                    if (!transaction.get().getAccNo().equals(accNo)) {
                        ResponseUtil.sendResponse(httpRequest, httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden", null);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
