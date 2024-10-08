package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.util.List;

public interface BankAccountService {
    BankAccount getBankAccountByAccNo(String accNo);
    List<BankAccount> getBankAccountsByUserId(Long userId);
    List<GetBankAccountDTO> getBankAccountsWithBankByUserId(Long userId);
    GetBankAccountDTO getBankAccountWithBankByAccNo(String accNo);
    BankAccount addBankAccount(BankAccount account);
    BankAccount updateBankAccount(BankAccount account);
    void deleteBankAccount(String accNo);
    void reopenBankAccount(String accNo);
    void closeBankAccount(String accNo);
    void updateAccountBalance(Transaction transaction) throws Exception;
    void rollbackAccountBalanceUpdate(Transaction transaction) throws Exception;
    void deposit(Transaction transaction) throws Exception;
    void withdraw(Transaction transaction) throws Exception;
}