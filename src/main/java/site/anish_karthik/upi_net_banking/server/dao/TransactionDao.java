package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.util.List;

public interface TransactionDao extends GenericDao<Transaction, Long> {
    List<Transaction> findByAccNo(String accNo);
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUpiId(String upiId);
    List<Transaction> findByCardNo(String cardNo);
    List<Transaction> findByReferenceId(String referenceId);
}
