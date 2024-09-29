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
public class CreateUpiDTO {
    private String accNo;
    private Integer upiPin;
    private Long userId;

    public Upi toUpi() {
        return Upi.builder()
                .accNo(accNo)
                .upiPinHashed(upiPin.toString())
                .userId(userId)
                .build();
    }

    public static CreateUpiDTO fromUpi(Upi upi) {
        return CreateUpiDTO.builder()
                .accNo(upi.getAccNo())
                .userId(upi.getUserId())
                .build();
    }
}