package site.anish_karthik.upi_net_banking.server.command;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;

public class WithdrawCommand implements Command {
    private final Transaction transaction;

    public WithdrawCommand(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void execute() {
        // Logic for withdraw
        System.out.println("Withdrawing " + transaction.getAmount() + " from account " + transaction.getAccNo());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
    }
}
