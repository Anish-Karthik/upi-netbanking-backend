package site.anish_karthik.upi_net_banking.server.command;

import site.anish_karthik.upi_net_banking.server.model.Transaction;

public class TransferCommand implements Command {
    private final Transaction withdrawTransaction;
    private final Transaction depositTransaction;

    public TransferCommand(Transaction withdrawTransaction, Transaction depositTransaction) {
        this.withdrawTransaction = withdrawTransaction;
        this.depositTransaction = depositTransaction;
    }

    @Override
    public void execute() {
        // Logic for transfer
        new WithdrawCommand(withdrawTransaction).execute();
        new DepositCommand(depositTransaction).execute();
    }
}
