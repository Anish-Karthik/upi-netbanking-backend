package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.account.UpdateAccountBalanceCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transaction.CreateTransactionCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.TransferInvoker;
import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransactionDaoImpl;
import site.anish_karthik.upi_net_banking.server.factories.method.TransactionFactory;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.TransactionStrategy;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.sql.SQLException;
import java.util.List;

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
        System.out.println("TransactionServiceImpl createTransaction");
        return transactionDao.save(transaction);
    }

    @Override
    public Transaction makeTransaction(Transaction transaction) {
        TransferInvoker invoker = new TransferInvoker();
        Command createTransactionCommand = new CreateTransactionCommand(transaction);

        invoker.addCommand(createTransactionCommand);
        invoker.addCommand(executeTransactionCommand(transaction));

        try {
            invoker.executeSerially();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return transaction;
    }

    @Override
    public Command executeTransactionCommand(Transaction transaction) {
        return new UpdateAccountBalanceCommand(transaction, (t) -> {
             transactionDao.update(t);
             return null;
        });
    }

    @Override
    public Transaction handleTransaction(Transaction transaction, TransactionCategory category, String pin) throws Exception {
        TransactionStrategy transactionStrategy = transactionFactory.getStrategy(transaction.getPaymentMethod(), transaction.getTransactionType(),category);
        transaction = transactionStrategy.execute(transaction, this::makeTransaction);
        return transaction;
    }

    @Override
    public void deleteTransaction(Long transactionId) throws Exception {
        transactionDao.delete(transactionId);
    }

    @Override
    public void updateTransaction(Transaction transaction) throws Exception {
        transactionDao.update(transaction);
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
