// DepositCommand.java
package site.anish_karthik.upi_net_banking.server.command;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;

public class DepositCommand implements Command {
    private final Transaction transaction;

    public DepositCommand(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void execute() {
        // Logic for deposit
        System.out.println("Depositing " + transaction.getAmount() + " to account " + transaction.getAccNo());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
    }
}