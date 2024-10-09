package site.anish_karthik.upi_net_banking.server.command.impl.transaction;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.service.TransactionService;
import site.anish_karthik.upi_net_banking.server.service.impl.TransactionServiceImpl;

public class CreateTransactionCommand implements Command {
    private BankAccount account;
    private String status;
    private Transaction transaction;
    private TransactionService transactionService = new TransactionServiceImpl();

    public CreateTransactionCommand(BankAccount account, String status) {
        this.account = account;
        this.status = status;
    }

    @Override
    public void execute() throws Exception {
//        this.transaction = transactionService.createTransaction(this.account, this.status);
    }

    @Override
    public void undo() throws Exception {
//        updateTransaction(this.transaction, "CANCELLED");
    }

    public Transaction getTransaction() {
        return this.transaction;
    }
}

