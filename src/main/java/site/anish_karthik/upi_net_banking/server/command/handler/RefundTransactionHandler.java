package site.anish_karthik.upi_net_banking.server.command.handler;

import site.anish_karthik.upi_net_banking.server.command.impl.account.UpdateAccountBalanceCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transaction.CreateTransactionCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.TransferInvoker;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransactionDTO;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

public class RefundTransactionHandler {

    private static final int MAX_RETRIES = 15;
    private static final long INITIAL_INTERVAL = 1000; // 1 second

    public void refundTransaction(Transaction payerTransaction) {
        int retryCount = 0;
        long interval = INITIAL_INTERVAL;

        // Create a new transaction for the refund
        Transaction refundTransaction = CreateTransactionDTO.fromTransaction(payerTransaction).toTransaction();
        refundTransaction.setTransactionType(TransactionType.DEPOSIT);
        refundTransaction.setTransactionStatus(TransactionStatus.PROCESSING);

        while (retryCount < MAX_RETRIES) {
            try {
                TransferInvoker invoker = new TransferInvoker();
                invoker.addCommand(new CreateTransactionCommand(refundTransaction));
                invoker.addCommand(new UpdateAccountBalanceCommand(refundTransaction));
                invoker.executeSerially();
                System.out.println("Refund successful");
                return; // Exit the method if successful
            } catch (Exception e) {
                retryCount++;
                System.err.println("Refund failed, retrying in " + interval + "ms. Attempt: " + retryCount);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread was interrupted", ie);
                }
                interval *= 4; // Exponential backoff
            }
        }
        throw new RuntimeException("Refund failed after " + MAX_RETRIES + " attempts");
    }
}