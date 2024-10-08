package site.anish_karthik.upi_net_banking.server.service.impl;


import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.PaymentMethodService;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao = new BankAccountDaoImpl(DatabaseUtil.getConnection());
    private final List<PaymentMethodService> paymentMethodServices;

    public BankAccountServiceImpl() throws SQLException, ClassNotFoundException {
        try {
            this.paymentMethodServices = List.of(new CardServiceImpl(), new UpiServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public BankAccount getBankAccountByAccNo(String accNo) {
        return bankAccountDao.findById(accNo).orElse(null);
    }

    @Override
    public List<BankAccount> getBankAccountsByUserId(Long userId) {
        return bankAccountDao.findByUserId(userId);
    }

    @Override
    public List<GetBankAccountDTO> getBankAccountsWithBankByUserId(Long userId) {
        return bankAccountDao.findByUserIdWithBank(userId);
    }

    @Override
    public GetBankAccountDTO getBankAccountWithBankByAccNo(String accNo) {
        return bankAccountDao.findByIdWithBank(accNo).orElse(null);
    }

    @Override
    public BankAccount addBankAccount(BankAccount account) {
        return bankAccountDao.save(account);
    }

    @Override
    public BankAccount updateBankAccount(BankAccount account) {
        return bankAccountDao.update(account);
    }

    @Override
    public void deleteBankAccount(String accNo) {
        System.out.println("Deleting account: " + accNo);
        bankAccountDao.update(BankAccount.builder().accNo(accNo).status(AccountStatus.CLOSED).build());
        // deactivate all payment methods associated with this account
        paymentMethodServices.forEach(paymentMethodService -> {
            try {
                paymentMethodService.deactivateByAccNo(accNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void reopenBankAccount(String accNo) {
        bankAccountDao.update(BankAccount.builder().accNo(accNo).status(AccountStatus.ACTIVE).build());
    }

    @Override
    public void closeBankAccount(String accNo) {
        deleteBankAccount(accNo);
    }

    @Override
    public void updateAccountBalance(Transaction transaction) throws Exception {
        BigDecimal amount = transaction.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new Exception("Amount cannot be zero");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Amount cannot be negative");
        }
        if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            amount = amount.negate();
        }
        bankAccountDao.updateAccountBalance(transaction.getAccNo(), amount);
    }

    @Override
    public void rollbackAccountBalanceUpdate(Transaction transaction) throws Exception {
        BankAccount account = bankAccountDao.findByTransactionId(transaction.getTransactionId()).orElseThrow(() -> new Exception("Account not found"));
        if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
        bankAccountDao.update(account);
    }

    @Override
    public void deposit(Transaction transaction) throws Exception {
        BankAccount account = bankAccountDao.findByTransactionId(transaction.getTransactionId()).orElseThrow(() -> new Exception("Account not found"));
        account.setBalance(account.getBalance().add(transaction.getAmount()));
        bankAccountDao.update(account);
    }

    @Override
    public void withdraw(Transaction transaction) throws Exception {
        BankAccount account = bankAccountDao.findByTransactionId(transaction.getTransactionId()).orElseThrow(() -> new Exception("Account not found"));
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        bankAccountDao.update(account);
    }

}
