package site.anish_karthik.upi_net_banking.server.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String accNo;
    private String upiId;
    private String cardNo;
    private boolean canDeposit;
    private boolean canWithdraw;
    private boolean canSendMoney;
    private boolean canReceiveMoney;

    public Permission(boolean canDeposit, boolean canWithdraw, boolean canSendMoney, boolean canReceiveMoney) {
        this.canDeposit = canDeposit;
        this.canWithdraw = canWithdraw;
        this.canSendMoney = canSendMoney;
        this.canReceiveMoney = canReceiveMoney;
    }
}
