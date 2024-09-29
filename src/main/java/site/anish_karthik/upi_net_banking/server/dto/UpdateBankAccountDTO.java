package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBankAccountDTO {
    private AccountType accountType;
    private AccountStatus status;

    public static UpdateBankAccountDTO fromBankAccount(BankAccount bankAccount) {
        return UpdateBankAccountDTO.builder()
                .accountType(bankAccount.getAccountType())
                .status(bankAccount.getStatus())
                .build();
    }

    public BankAccount toBankAccount() {
        return BankAccount.builder()
                .accountType(accountType)
                .status(status)
                .build();
    }
}
