package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByAccNo(String accNo);
}
