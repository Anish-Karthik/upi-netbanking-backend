package site.anish_karthik.upi_net_banking.server.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private long id;
    private String accNo;
    private String upiId;
    private String cardNo;
    private boolean canDeposit;
    private boolean canWithdraw;
    private boolean canSendMoney;
    private boolean canReceiveMoney;
}
