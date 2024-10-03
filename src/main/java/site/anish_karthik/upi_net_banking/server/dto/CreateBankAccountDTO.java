package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateBankAccountDTO {
    private String accNo;
    private String ifsc;
    private Long bankId;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;

    public static CreateBankAccountDTO fromBankAccount(BankAccount bankAccount) {
        return CreateBankAccountDTO.builder()
                .accNo(bankAccount.getAccNo())
                .ifsc(bankAccount.getIfsc())
                .bankId(bankAccount.getBankId())
                .balance(bankAccount.getBalance())
                .accountType(bankAccount.getAccountType())
                .status(bankAccount.getStatus())
                .build();
    }

    public BankAccount toBankAccount() {
        return BankAccount.builder()
                .accNo(accNo)
                .ifsc(ifsc)
                .bankId(bankId)
                .balance(balance)
                .accountType(accountType)
                .status(status)
                .build();
    }
}
