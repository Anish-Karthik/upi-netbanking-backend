package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.Bank;

import java.util.List;

public interface BankService {
    void createBank(Bank bank);
    Bank getBankById(long id);
    List<Bank> getAllBanks();
    void updateBank(Bank bank);
    void deleteBank(long id);
}
