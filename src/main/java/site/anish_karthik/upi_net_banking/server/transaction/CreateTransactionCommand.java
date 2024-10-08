// CreateTransactionCommand.java
package site.anish_karthik.upi_net_banking.server.transaction;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;

public class CreateTransactionCommand implements TransactionCommand {
    private final TransactionService transactionService;
    private final Transaction transaction;

    public CreateTransactionCommand(TransactionService transactionService, Transaction transaction) {
        this.transactionService = transactionService;
        this.transaction = transaction;
    }

    @Override
    public void execute() throws Exception {
        transactionService.createTransaction(transaction);
    }

    @Override
    public void rollback() throws Exception {
        transactionService.deleteTransaction(transaction.getTransactionId());
    }

    public Transaction getTransaction() {
        return transaction;
    }
}