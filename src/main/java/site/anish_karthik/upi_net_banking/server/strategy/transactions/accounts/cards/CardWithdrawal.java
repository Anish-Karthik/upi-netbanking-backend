package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.cards;

import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

public class CardWithdrawal extends CardBaseStrategy {
    public CardWithdrawal(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
    }
}