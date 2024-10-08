package site.anish_karthik.upi_net_banking.server.transaction;

// DebitPayerAccountCommand.java

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.BankService;

public class DebitPayerAccountCommand implements TransactionCommand {
    private final BankAccountService bankService;
    private final Transaction transaction;

    public DebitPayerAccountCommand(BankAccountService bankService, Transaction transaction) {
        this.bankService = bankService;
        this.transaction = transaction;
    }

    @Override
    public void execute() throws Exception {
        bankService.updateAccountBalance(transaction);
    }

    @Override
    public void rollback() throws Exception {
        bankService.rollbackAccountBalanceUpdate(transaction);
    }
}