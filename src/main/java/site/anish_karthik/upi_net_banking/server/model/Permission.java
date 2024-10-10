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
    private Boolean canDeposit;
    private Boolean canWithdraw;
    private Boolean canSendMoney;
    private Boolean canReceiveMoney;

    public Permission(boolean canDeposit, boolean canWithdraw, boolean canSendMoney, boolean canReceiveMoney) {
        this.canDeposit = canDeposit;
        this.canWithdraw = canWithdraw;
        this.canSendMoney = canSendMoney;
        this.canReceiveMoney = canReceiveMoney;
    }
}
