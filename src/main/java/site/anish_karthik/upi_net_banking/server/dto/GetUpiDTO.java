package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.model.enums.UpiStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUpiDTO {
    private String upiId;
    private Long userId;
    private String accNo;
    private UpiStatus status;
    private Boolean isDefault;

    public static GetUpiDTO fromUpi(Upi upi) {
        return GetUpiDTO.builder()
                .upiId(upi.getUpiId())
                .userId(upi.getUserId())
                .accNo(upi.getAccNo())
                .status(upi.getStatus())
                .isDefault(upi.getIsDefault())
                .build();
    }

    public Upi toUpi() {
        return Upi.builder()
                .upiId(upiId)
                .userId(userId)
                .accNo(accNo)
                .status(status)
                .isDefault(isDefault)
                .build();
    }
}
