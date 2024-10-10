package site.anish_karthik.upi_net_banking.server.command.impl.transaction;

import lombok.Getter;
import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.service.impl.TransactionServiceImpl;

public class UpdateTransactionCommand implements Command {
    private final Transaction transaction;
    private final TransactionService transactionService = new TransactionServiceImpl();

    public UpdateTransactionCommand(Transaction transaction, TransactionStatus status) {
        this.transaction = transaction;
        transaction.setTransactionStatus(status);
    }

    @Override
    public void execute() throws Exception {
        transactionService.updateTransaction(transaction);
    }

    @Override
    public void undo() throws Exception {
    }
}

