package site.anish_karthik.upi_net_banking.server.strategy.pin;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.strategy.pin.impl.CompositeOrPinRequirementStrategy;
import site.anish_karthik.upi_net_banking.server.strategy.pin.impl.TransferDepositPinRequirementStrategy;

import java.util.List;

public class PinRequirementContext {
    private final CompositeOrPinRequirementStrategy compositeStrategy;

    public PinRequirementContext() {
        this.compositeStrategy = new CompositeOrPinRequirementStrategy();
        this.compositeStrategy.addStrategy(new TransferDepositPinRequirementStrategy());

        // Add other strategies as needed
    }

    public PinRequirementContext(CompositeOrPinRequirementStrategy compositeStrategy) {
        this.compositeStrategy = compositeStrategy;
    }

    public PinRequirementContext(List<PinRequirementStrategy> strategies) {
        this.compositeStrategy = new CompositeOrPinRequirementStrategy();
        strategies.forEach(this.compositeStrategy::addStrategy);
    }

    public PinRequirementContext(PinRequirementStrategy... strategies) {
        this.compositeStrategy = new CompositeOrPinRequirementStrategy();
        for (PinRequirementStrategy strategy : strategies) {
            this.compositeStrategy.addStrategy(strategy);
        }
    }

    public PinRequirementContext addStrategy(PinRequirementStrategy strategy) {
        this.compositeStrategy.addStrategy(strategy);
        return this;
    }

    public boolean isPinNotRequired(Transaction transaction, TransactionCategory transactionCategory) {
        return compositeStrategy.isPinNotRequired(transaction, transactionCategory);
    }
}