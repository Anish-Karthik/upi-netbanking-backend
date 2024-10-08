package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

import java.util.function.Function;

public class AccountWithdrawal implements TransactionStrategy {
    private final BankAccountService bankAccountService;

    public AccountWithdrawal(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public Boolean verifyPermission(Transaction transaction) throws Exception {
//        return bankAccountService.verifyPermission(transaction);
        return true;
    }

    @Override
    public void execute(Transaction transaction) throws Exception {
        // Logic for account withdrawal
        if (!verifyPermission(transaction)) {
            throw new RuntimeException("No permission found");
        }
        bankAccountService.withdraw(transaction);
    }

    @Override
    public Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception {
        if (!verifyPermission(transaction)) {
            throw new RuntimeException("No permission found");
        }
        return handle.apply(transaction);
    }
}
