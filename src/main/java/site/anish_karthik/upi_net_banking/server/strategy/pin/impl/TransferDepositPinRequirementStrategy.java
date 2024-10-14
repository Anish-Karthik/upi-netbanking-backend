package site.anish_karthik.upi_net_banking.server.strategy.pin.impl;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;
import site.anish_karthik.upi_net_banking.server.strategy.pin.PinRequirementStrategy;

public class TransferDepositPinRequirementStrategy implements PinRequirementStrategy {
    @Override
    public boolean isPinNotRequired(Transaction transaction, TransactionCategory transactionCategory) {
        return (transactionCategory == TransactionCategory.TRANSFER && transaction.getTransactionType() == TransactionType.DEPOSIT);
    }
}