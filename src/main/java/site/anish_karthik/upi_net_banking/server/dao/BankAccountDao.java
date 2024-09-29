package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountDao extends GenericDao<BankAccount, String> {
    List<BankAccount> findByUserId(long userId);
    List<GetBankAccountDTO> findByUserIdWithBank(long userId);
    Optional<GetBankAccountDTO> findByIdWithBank(String accNo);
}
