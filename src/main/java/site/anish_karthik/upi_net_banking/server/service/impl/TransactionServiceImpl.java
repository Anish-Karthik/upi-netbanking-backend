package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransactionDaoImpl;
import site.anish_karthik.upi_net_banking.server.factories.method.TransactionFactory;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.TransactionStrategy;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {
    private final BankAccountService bankAccountService;
    private final TransactionDao transactionDao;
    private final TransactionFactory transactionFactory;

    public TransactionServiceImpl() {
        try {
            this.bankAccountService = new BankAccountServiceImpl();
            this.transactionFactory = new TransactionFactory(bankAccountService);
            this.transactionDao = new TransactionDaoImpl(DatabaseUtil.getConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction createTransaction(Transaction transaction) throws Exception {
        return transactionDao.save(transaction);
    }

    @Override
    public Transaction makeTransaction(Transaction transaction) {
//        bankAccountService.deductAmount(transaction);
        transaction = transactionDao.save(transaction);
        try {
            bankAccountService.updateAccountBalance(transaction);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }
        transaction.setEndedAt(new Timestamp(System.currentTimeMillis()));
        transactionDao.update(transaction);
        return transaction;
    }

    @Override
    public Transaction handleTransaction(Transaction transaction, TransactionCategory category) throws Exception {
        TransactionStrategy transactionStrategy = transactionFactory.getStrategy(transaction.getPaymentMethod(), transaction.getTransactionType(),category);
        transaction = transactionStrategy.execute(transaction, this::makeTransaction);
        return transaction;
    }

    @Override
    public void deleteTransaction(Long transactionId) throws Exception {
        transactionDao.delete(transactionId);
    }

    @Override
    public void updateTransactionStatus(Long transactionId, boolean status) throws Exception {
        Optional<Transaction> transaction = transactionDao.findById(transactionId);
        if (transaction.isPresent()) {
            transaction.get().setTransactionStatus(TransactionStatus.SUCCESS);
            transactionDao.update(transaction.get());
        }
    }

    @Override
    public Transaction getTransactionById(Long transactionId) throws Exception {
        return transactionDao.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getTransactionsByAccNo(String accNo) throws Exception {
        return transactionDao.findByAccNo(accNo);
    }



}
