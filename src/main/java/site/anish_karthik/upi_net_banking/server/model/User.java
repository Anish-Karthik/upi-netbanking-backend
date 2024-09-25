package site.anish_karthik.upi_net_banking.server.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private long id;
    private String phone;
    private String email;
    private String name;
    private Date dob;
    private String address;
    private String password;
}
