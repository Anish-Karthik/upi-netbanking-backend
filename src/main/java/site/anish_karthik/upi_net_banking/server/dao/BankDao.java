package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Bank;

public interface BankDao extends GenericDao<Bank, Long> {
    Bank findByCode(String code);
}
