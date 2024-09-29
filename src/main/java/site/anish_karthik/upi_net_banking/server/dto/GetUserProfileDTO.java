package site.anish_karthik.upi_net_banking.server.dto;

import lombok.*;
import site.anish_karthik.upi_net_banking.server.model.User;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUserProfileDTO {
    private String name;
    private Date dob;
    private String address;
    private String phone;
    private String email;

    public User toUser() {
        return User.builder()
                .name(name)
                .dob(dob)
                .address(address)
                .phone(phone)
                .email(email)
                .build();
    }

    public static GetUserProfileDTO fromUser(User user) {
        return GetUserProfileDTO.builder()
                .name(user.getName())
                .dob(user.getDob())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}
