package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction) throws Exception;
    Transaction makeTransaction(Transaction transaction) throws Exception;
    Transaction handleTransaction(Transaction transaction, TransactionCategory category, String pin) throws Exception;
    void deleteTransaction(Long transactionId) throws Exception;
    void updateTransaction(Transaction transaction) throws Exception;
    Transaction getTransactionById(Long transactionId) throws Exception;
    List<Transaction> getTransactionsByAccNo(String accNo) throws Exception;
}
