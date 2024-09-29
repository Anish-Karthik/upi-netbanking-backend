package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Builder
@Data
public class CreateBankAccountDTO {
    private String accNo;
    private String ifsc;
    private Long bankId;
    private Long userId;
    private BigDecimal balance;
    private Timestamp createdAt;
    private AccountType accountType;
    private AccountStatus status;

    public static CreateBankAccountDTO fromBankAccount(BankAccount bankAccount) {
        return CreateBankAccountDTO.builder()
                .accNo(bankAccount.getAccNo())
                .ifsc(bankAccount.getIfsc())
                .bankId(bankAccount.getBankId())
                .userId(bankAccount.getUserId())
                .balance(bankAccount.getBalance())
                .createdAt(bankAccount.getCreatedAt())
                .accountType(bankAccount.getAccountType())
                .status(bankAccount.getStatus())
                .build();
    }

    public BankAccount toBankAccount() {
        return BankAccount.builder()
                .accNo(accNo)
                .ifsc(ifsc)
                .bankId(bankId)
                .userId(userId)
                .balance(balance)
                .createdAt(createdAt)
                .accountType(accountType)
                .status(status)
                .build();
    }
}
