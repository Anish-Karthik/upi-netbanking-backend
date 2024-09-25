package site.anish_karthik.upi_net_banking.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Upi {
    private String upiId;
    private Long bankAccId;
    private String upiPinHashed;
    private Long userId;
    private Boolean isDefault;
    private Boolean isMerchant;
}
