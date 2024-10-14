package site.anish_karthik.upi_net_banking.server.strategy.pin;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;

public interface PinRequirementStrategy {
    boolean isPinRequired(Transaction transaction, TransactionCategory transactionCategory);
}