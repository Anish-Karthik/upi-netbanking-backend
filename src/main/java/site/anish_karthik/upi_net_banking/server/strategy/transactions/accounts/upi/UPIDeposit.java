package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.upi;

import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

public class UPIDeposit extends UpiBaseStrategy {
    public UPIDeposit(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
    }
}