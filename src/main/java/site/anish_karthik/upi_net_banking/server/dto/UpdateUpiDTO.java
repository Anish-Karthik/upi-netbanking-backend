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
public class UpdateUpiDTO {
    private UpiStatus status;

    public Upi toUpi() {
        return Upi.builder()
                .status(status)
                .build();
    }

    public static UpdateUpiDTO fromUpi(Upi upi) {
        return UpdateUpiDTO.builder()
                .status(upi.getStatus())
                .build();
    }
}

