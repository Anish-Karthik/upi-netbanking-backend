package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.util.List;

public interface TransactionDao extends GenericDao<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByAccNo(String accNo);
    List<Transaction> findByUpiId(String upiId);
    List<Transaction> findByCard(String status);
    List<Transaction> findByReferenceId(String referenceId);
}
