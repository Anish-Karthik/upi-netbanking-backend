package site.anish_karthik.upi_net_banking.server.command.impl.account;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransactionDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

public class UpdateAccountBalanceCommand implements Command {
    private final Transaction transaction;
    private final BankAccountService bankAccountService;
    private final TransactionDao transactionDao;

    public UpdateAccountBalanceCommand(Transaction transaction) {
        this.transaction = transaction;
        try {
            this.bankAccountService = new BankAccountServiceImpl();
            this.transactionDao = new TransactionDaoImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute() throws Exception {
        System.out.println("UpdateAccountBalanceCommand execute" + transaction);
        bankAccountService.updateAccountBalance(transaction);
        System.out.println("2::UpdateAccountBalanceCommand execute" + transaction);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setEndedAt(Timestamp.from(java.time.Instant.now()));
        transactionDao.update(transaction);
        System.out.println("UpdateAccountBalanceCommand execute" + transaction);
    }

    @Override
    public void undo() throws Exception {
        // DB level transaction management is handled, so no need to rollback here for account balance
        transaction.setTransactionStatus(TransactionStatus.FAILURE);
        transaction.setEndedAt(Timestamp.from(java.time.Instant.now()));
        transactionDao.update(transaction);
    }

}