package site.anish_karthik.upi_net_banking.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Bank {
    private String name;
    private String code;
    private long id;
}
