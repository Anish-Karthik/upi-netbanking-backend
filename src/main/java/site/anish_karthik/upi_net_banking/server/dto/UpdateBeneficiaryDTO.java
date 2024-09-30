package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateBeneficiaryDTO {
    private String name;
    private String accNo;
    private String description;
    private String upiId;

    public static CreateBeneficiaryDTO fromBeneficiary(Beneficiary beneficiary) {
        return CreateBeneficiaryDTO.builder()
                .name(beneficiary.getName())
                .accNo(beneficiary.getAccNo())
                .description(beneficiary.getDescription())
                .upiId(beneficiary.getUpiId())
                .build();
    }

    public Beneficiary toBeneficiary() {
        return Beneficiary.builder()
                .name(name)
                .accNo(accNo)
                .description(description)
                .upiId(upiId)
                .build();
    }
}