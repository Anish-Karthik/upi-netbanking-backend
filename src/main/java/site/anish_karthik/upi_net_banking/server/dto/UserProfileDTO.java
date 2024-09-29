package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.User;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileDTO {
    private String name;
    private Date dob;
    private String address;

    public User toUser() {
        return User.builder()
                .name(name)
                .dob(dob)
                .address(address)
                .build();
    }

    public static UserProfileDTO fromUser(User user) {
        return UserProfileDTO.builder()
                .name(user.getName())
                .dob(user.getDob())
                .address(user.getAddress())
                .build();
    }
}
