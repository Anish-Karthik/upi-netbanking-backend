package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.BankAccount;

import java.util.List;

public interface BankAccountDao extends GenericDao<BankAccount, String> {
    List<BankAccount> findByUserId(long userId);
}
