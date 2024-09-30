package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Permission;
import java.util.Optional;

public interface PermissionDao {
    Permission save(Permission permission);
    Optional<Permission> findById(Long id);
    Optional<Permission> findByUpiId(String upiId);
    Optional<Permission> findByCardNo(String cardNo);
    Optional<Permission> findByAccNo(String accNo);
}