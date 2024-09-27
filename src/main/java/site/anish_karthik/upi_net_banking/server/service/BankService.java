package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.Bank;

import java.util.List;
import java.util.Optional;

public interface BankService {
    Bank createBank(Bank bank);
    Optional<Bank> getBankById(long id);
    List<Bank> getAllBanks();
    Bank updateBank(Bank bank);
    void deleteBank(long id);
}
