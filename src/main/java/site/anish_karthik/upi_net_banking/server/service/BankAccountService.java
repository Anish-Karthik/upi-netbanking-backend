package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount getBankAccountByAccNo(String accNo);
    List<BankAccount> getBankAccountsByUserId(long userId);
    BankAccount addBankAccount(BankAccount account);
    BankAccount updateBankAccount(BankAccount account);
    void deleteBankAccount(String accNo);
}