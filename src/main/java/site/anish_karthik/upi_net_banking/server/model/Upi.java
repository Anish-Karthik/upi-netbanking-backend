package site.anish_karthik.upi_net_banking.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.enums.UpiStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Upi {
    private String upiId;
    private String accNo;
    private String upiPinHashed;
    private Long userId;
    private Boolean isDefault;
    private UpiStatus status;
}
