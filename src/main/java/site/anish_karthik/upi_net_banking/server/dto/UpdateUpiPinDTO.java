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
public class UpdateUpiPinDTO {
    private Integer upiPin;
    private Integer oldPin;

    public Upi toUpi() {
        return Upi.builder()
                .upiPinHashed(upiPin.toString()).build();
    }
}
