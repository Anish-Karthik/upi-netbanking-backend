package site.anish_karthik.upi_net_banking.server.command.handler;

import site.anish_karthik.upi_net_banking.server.command.invoker.TransferInvoker;
import site.anish_karthik.upi_net_banking.server.command.impl.account.CreditAccountCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.account.DebitAccountCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transaction.CreateTransactionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transfer.CreateBankTransferCommand;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransferDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

public class BankTransferHandler {
    public static void handleBankTransfer(BankAccount A, BankAccount B, CreateTransferDTO data) {
        TransferInvoker invoker = new TransferInvoker();

        CreateTransactionCommand createTransactionA = new CreateTransactionCommand(A, "PROCESSING");
        CreateTransactionCommand createTransactionB = new CreateTransactionCommand(B, "PROCESSING");

        invoker.addCommand(createTransactionA);
        invoker.addCommand(createTransactionB);

        try {
            invoker.executeInParallel();
        } catch (Exception e) {
            System.err.println("Error handling bank transfer: " + e.getMessage());
            return;
        }

        Transaction T1 = createTransactionA.getTransaction();
        Transaction T2 = createTransactionB.getTransaction();

        CreateBankTransferCommand createBankTransfer = new CreateBankTransferCommand(T1, T2, "PROCESSING");
        invoker.addCommand(createBankTransfer);

        DebitAccountCommand debitAccountA = new DebitAccountCommand(A, T1);
        invoker.addCommand(debitAccountA);

        try {
            invoker.executeSerially();
        } catch (Exception e) {
            System.err.println("Error handling bank transfer: " + e.getMessage());
            return;
        }

        CreditAccountCommand creditAccountB = new CreditAccountCommand(B, T2);
        invoker.addCommand(creditAccountB);

        try {
            invoker.executeSerially();
        } catch (Exception e) {
            RetryHandler.retryCreateRefund(A, T1, 5);
            System.err.println("Error handling bank transfer: " + e.getMessage());
        }
    }
}