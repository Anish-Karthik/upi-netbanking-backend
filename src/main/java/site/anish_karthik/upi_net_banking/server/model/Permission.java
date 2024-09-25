package site.anish_karthik.upi_net_banking.server.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String bankAccountId;
    private String upiId;
    private Long cardId;
    private Boolean canDeposit;
    private Boolean canWithdraw;
    private Boolean canSendMoney;
    private Boolean canReceiveMoney;
}

