package site.anish_karthik.upi_net_banking.server.strategy.pin.impl;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.strategy.pin.PinRequirementStrategy;

import java.util.ArrayList;
import java.util.List;

public class CompositeOrPinRequirementStrategy implements PinRequirementStrategy {
    private final List<PinRequirementStrategy> strategies = new ArrayList<>();

    public void addStrategy(PinRequirementStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public boolean isPinNotRequired(Transaction transaction, TransactionCategory transactionCategory) {
        for (PinRequirementStrategy strategy : strategies) {
            if (strategy.isPinNotRequired(transaction, transactionCategory)) {
                return true;
            }
        }
        return false;
    }
}