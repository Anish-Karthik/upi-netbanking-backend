package site.anish_karthik.upi_net_banking.server.command.handler;

import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.util.concurrent.TimeUnit;

public class RetryHandler {
    public static void retryCreateRefund(BankAccount account, Transaction transaction, int maxRetries) {
        int attempt = 0;
        long interval = TimeUnit.MINUTES.toMillis(20); // Initial interval of 20 minutes

        while (attempt < maxRetries) {
            try {
//                createRefund(account, transaction);
                System.out.println("Refund created successfully");
                return;
            } catch (Exception e) {
                attempt++;
                System.err.println("Attempt " + attempt + " failed. Retrying in " + TimeUnit.MILLISECONDS.toMinutes(interval) + " minutes...");
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                interval = Math.min(interval * 2, TimeUnit.MINUTES.toMillis(60)); // Cap the interval at 60 minutes
            }
        }

        System.err.println("Max retries reached. Refund creation failed.");
    }
}