package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Upi;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUpiDTO {
    private String upiId;
    private Long userId;
    private String accNo;

    public static GetUpiDTO fromUpi(Upi upi) {
        return GetUpiDTO.builder()
                .upiId(upi.getUpiId())
                .userId(upi.getUserId())
                .accNo(upi.getAccNo())
                .build();
    }

    public Upi toUpi() {
        return Upi.builder()
                .upiId(upiId)
                .userId(userId)
                .accNo(accNo)
                .build();
    }
}
