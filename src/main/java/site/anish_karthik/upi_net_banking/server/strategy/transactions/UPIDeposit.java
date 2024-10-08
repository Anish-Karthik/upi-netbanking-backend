package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

import java.util.function.Function;

public class UPIDeposit implements TransactionStrategy {
    private final BankAccountService bankAccountService;

    public UPIDeposit(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void execute(Transaction transaction) throws Exception {
        // Logic for UPI deposit
        bankAccountService.deposit(transaction);
    }

    @Override
    public Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception {
        return handle.apply(transaction);
    }
}