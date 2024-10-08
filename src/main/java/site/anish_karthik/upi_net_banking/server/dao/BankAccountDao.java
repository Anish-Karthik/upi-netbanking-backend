package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankAccountDao extends GenericDao<BankAccount, String> {
    List<BankAccount> findByUserId(Long userId);
    List<GetBankAccountDTO> findByUserIdWithBank(Long userId);
    Optional<GetBankAccountDTO> findByIdWithBank(String accNo);
    Optional<BankAccount> findByTransactionId(Long transactionId);
    void updateAccountBalance(String accNo, BigDecimal amount) throws Exception;
}