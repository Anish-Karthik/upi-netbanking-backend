package site.anish_karthik.upi_net_banking.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Beneficiary {
    private Long id;
    private String name;
    private String accNo;
    private Long beneficiaryOfUserId;
    private String description;
    private String upiId;
}
