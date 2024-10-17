package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.UserWithUPI;
import site.anish_karthik.upi_net_banking.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Update user profile{
    User updateUser(User user);
    Optional<User> getUserById(Long id);
    List<UserWithUPI> getAllUsers(String search, Integer page, Integer size);
}
