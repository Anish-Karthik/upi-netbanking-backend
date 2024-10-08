package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.util.function.Function;

public interface TransactionStrategy {
    void execute(Transaction transaction) throws Exception;
//    takes in a CallBack function which takes in a Transaction object and returns a Transaction object
    Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception;
}