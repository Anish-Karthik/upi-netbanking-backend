package site.anish_karthik.upi_net_banking.server.command.impl.account;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

public class DebitAccountCommand implements Command {
    private BankAccount account;
    private Transaction transaction;

    public DebitAccountCommand(BankAccount account, Transaction transaction) {
        this.account = account;
        this.transaction = transaction;
    }

    @Override
    public void execute() throws Exception {
//        debitFromAccount(this.account, this.transaction);
//        updateTransaction(this.transaction, "SUCCESS");
    }

    @Override
    public void undo() throws Exception {
//        updateTransaction(this.transaction, "FAILED");
    }
}
