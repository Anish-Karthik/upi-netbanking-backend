package site.anish_karthik.upi_net_banking.server.command.impl.account;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.impl.BankAccountServiceImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.function.Function;

public class UpdateAccountBalanceCommand implements Command {
    private final Transaction transaction;
    private final BankAccountService bankAccountService;
    private final Function<Transaction, Void> updateTransaction;

    public UpdateAccountBalanceCommand(Transaction transaction, Function<Transaction, Void> updateTransaction) {
        this.transaction = transaction;
        this.updateTransaction = updateTransaction;
        try {
            this.bankAccountService = new BankAccountServiceImpl();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute() throws Exception {
        System.out.println("UpdateAccountBalanceCommand execute"+transaction);
        bankAccountService.updateAccountBalance(transaction);
        System.out.println("2::UpdateAccountBalanceCommand execute"+transaction);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setEndedAt(new Timestamp(System.currentTimeMillis()));
        updateTransaction.apply(transaction);
        System.out.println("UpdateAccountBalanceCommand execute"+transaction);
    }

    @Override
    public void undo() throws Exception {
        // DB level transaction management is handled, so no need to rollback here
        transaction.setTransactionStatus(TransactionStatus.FAILURE);
        transaction.setEndedAt(new Timestamp(System.currentTimeMillis()));
        updateTransaction.apply(transaction);
    }

}