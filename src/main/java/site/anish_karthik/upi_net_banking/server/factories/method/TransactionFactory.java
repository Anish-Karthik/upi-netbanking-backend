package site.anish_karthik.upi_net_banking.server.factories.method;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.*;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionFactory {
    private final BankAccountService bankAccountService;
    private final Map<TransactionKey, TransactionStrategy> strategyMap = new HashMap<>();

    public TransactionFactory(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
        initializeStrategies();
    }

    private void initializeStrategies() {
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.UPI, TransactionType.DEPOSIT, TransactionCategory.SOLO), new UPIDeposit(bankAccountService, TransactionCategory.SOLO));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.CARD, TransactionType.DEPOSIT, TransactionCategory.SOLO), new CardDeposit(bankAccountService, TransactionCategory.SOLO));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.CARD, TransactionType.WITHDRAWAL, TransactionCategory.SOLO), new CardWithdrawal(bankAccountService, TransactionCategory.SOLO));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.ACCOUNT, TransactionType.DEPOSIT, TransactionCategory.SOLO), new AccountDeposit(bankAccountService, TransactionCategory.SOLO));

        // Transaction Category - Transfer
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.UPI, TransactionType.DEPOSIT, TransactionCategory.TRANSFER), new UPIDeposit(bankAccountService, TransactionCategory.TRANSFER));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.UPI, TransactionType.WITHDRAWAL, TransactionCategory.TRANSFER), new UPIWithdrawal(bankAccountService, TransactionCategory.TRANSFER));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.CARD, TransactionType.WITHDRAWAL, TransactionCategory.TRANSFER), new CardWithdrawal(bankAccountService, TransactionCategory.TRANSFER));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.ACCOUNT, TransactionType.DEPOSIT, TransactionCategory.TRANSFER), new AccountDeposit(bankAccountService, TransactionCategory.TRANSFER));
        strategyMap.put(new TransactionKey(Transaction.PaymentMethod.ACCOUNT, TransactionType.WITHDRAWAL, TransactionCategory.TRANSFER), new AccountWithdrawal(bankAccountService, TransactionCategory.TRANSFER));
    }

    public TransactionStrategy getStrategy(Transaction.PaymentMethod paymentMethod, TransactionType transactionType, TransactionCategory transactionCategory) {
        TransactionStrategy strategy = strategyMap.get(new TransactionKey(paymentMethod, transactionType, transactionCategory));
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment method, transaction type, or transaction category");
        }
        return strategy;
    }

    private static class TransactionKey {
        private final Transaction.PaymentMethod paymentMethod;
        private final TransactionType transactionType;
        private final TransactionCategory transactionCategory;

        public TransactionKey(Transaction.PaymentMethod paymentMethod, TransactionType transactionType, TransactionCategory transactionCategory) {
            this.paymentMethod = paymentMethod;
            this.transactionType = transactionType;
            this.transactionCategory = transactionCategory;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TransactionKey that = (TransactionKey) o;
            return Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(transactionType, that.transactionType) && Objects.equals(transactionCategory, that.transactionCategory);
        }

        @Override
        public int hashCode() {
            return Objects.hash(paymentMethod, transactionType, transactionCategory);
        }
    }
}