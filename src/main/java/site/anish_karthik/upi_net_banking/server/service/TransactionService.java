package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction) throws Exception;
    Transaction makeTransaction(Transaction transaction) throws Exception;
    Transaction handleTransaction(Transaction transaction, TransactionCategory category) throws Exception;
    void deleteTransaction(Long transactionId) throws Exception;
    void updateTransactionStatus(Long transactionId, boolean status) throws Exception;
    Transaction getTransactionById(Long transactionId) throws Exception;
    List<Transaction> getTransactionsByAccNo(String accNo) throws Exception;
}
