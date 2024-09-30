package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.Permission;

public interface PermissionService {
    Permission savePermission(Permission permission);
    Permission getPermissionById(Long id);
    Permission getPermissionByUpiId(String upiId);
    Permission getPermissionByCardNo(String cardNo);
    Permission getPermissionByAccNo(String accNo);
}