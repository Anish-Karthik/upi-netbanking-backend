package site.anish_karthik.upi_net_banking.server.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserWithUPI {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> upiIds;
}
