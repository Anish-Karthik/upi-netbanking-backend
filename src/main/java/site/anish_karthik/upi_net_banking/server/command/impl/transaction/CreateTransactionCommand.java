package site.anish_karthik.upi_net_banking.server.command.impl.transaction;

import lombok.Getter;
import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.service.impl.TransactionServiceImpl;

import java.sql.Timestamp;

public class CreateTransactionCommand implements Command {
    @Getter
    private Transaction transaction;
    private final TransactionService transactionService = new TransactionServiceImpl();

    public CreateTransactionCommand(Transaction transaction) {
        System.out.println("CreateTransactionCommand");
        this.transaction = transaction;
    }

    @Override
    public void execute() throws Exception {
        System.out.println("CreateTransactionCommand execute");
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        transaction.setStartedAt(Timestamp.from(java.time.Instant.now()));
        this.transaction = transactionService.createTransaction(transaction);
        System.out.println("Transaction created: " + transaction);
    }

    @Override
    public void undo() throws Exception {
        transaction.setTransactionStatus(TransactionStatus.CANCELLED);
        transaction.setEndedAt(Timestamp.from(java.time.Instant.now()));
        transactionService.updateTransaction(transaction);
    }

}

