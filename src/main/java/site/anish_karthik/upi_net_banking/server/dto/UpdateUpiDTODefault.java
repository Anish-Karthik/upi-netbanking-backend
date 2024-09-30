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
public class UpdateUpiDTODefault {
    private Boolean isDefault;

    public Upi toUpi() {
        return Upi.builder()
                .isDefault(isDefault)
                .build();
    }

    public static UpdateUpiDTODefault fromUpi(Upi upi) {
        return UpdateUpiDTODefault.builder()
                .isDefault(upi.getIsDefault())
                .build();
    }
}
