package site.anish_karthik.upi_net_banking.server.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BankAccount {
    private String accNo;
    private String ifsc;
    private Long bankId;
    private Long userId;
    private BigDecimal balance;
    private Timestamp createdAt;
    private AccountType accountType;
    private AccountStatus status;
}
