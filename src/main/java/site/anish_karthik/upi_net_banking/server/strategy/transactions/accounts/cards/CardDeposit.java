package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.cards;

import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

public class CardDeposit extends CardBaseStrategy {
    public CardDeposit(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
    }
}