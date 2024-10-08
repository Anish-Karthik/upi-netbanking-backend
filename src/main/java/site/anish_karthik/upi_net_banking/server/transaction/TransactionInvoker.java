package site.anish_karthik.upi_net_banking.server.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionInvoker {
    private final List<TransactionCommand> transactionCommands = new ArrayList<>();

    public void addCommand(TransactionCommand command) {
        transactionCommands.add(command);
    }

    public void executeCommands() throws Exception {
        try {
            for (TransactionCommand command : transactionCommands) {
                command.execute();
            }
        } catch (Exception e) {
            rollbackCommands();
            throw e;
        }
    }

    private void rollbackCommands() throws Exception {
        for (TransactionCommand command : transactionCommands) {
            command.rollback();
        }
    }
}