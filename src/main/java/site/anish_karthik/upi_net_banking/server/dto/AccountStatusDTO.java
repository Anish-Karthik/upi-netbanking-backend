package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountStatusDTO {
    private AccountStatus status;

    public static AccountStatusDTO fromBankAccount(BankAccount bankAccount) {
        return AccountStatusDTO.builder()
                .status(bankAccount.getStatus())
                .build();
    }

    public BankAccount toBankAccount() {
        return BankAccount.builder()
                .status(status)
                .build();
    }
}
