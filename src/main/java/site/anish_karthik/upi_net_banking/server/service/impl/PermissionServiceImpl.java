package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.dao.impl.PermissionDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.dao.PermissionDao;
import site.anish_karthik.upi_net_banking.server.service.PermissionService;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.sql.SQLException;

public class PermissionServiceImpl implements PermissionService {
    private final PermissionDao permissionDao;

    public PermissionServiceImpl() {
        this.permissionDao = new PermissionDaoImpl();
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionDao.save(permission);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionDao.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public Permission getPermissionByUpiId(String upiId) {
        return permissionDao.findByUpiId(upiId).orElseThrow(() -> new RuntimeException("Permissions not found"));
    }

    @Override
    public Permission getPermissionByCardNo(String cardNo) {
        return permissionDao.findByCardNo(cardNo).orElseThrow(() -> new RuntimeException("Permissions not found"));
    }

    @Override
    public Permission getPermissionByAccNo(String accNo) {
        return permissionDao.findByAccNo(accNo).orElseThrow(() -> new RuntimeException("Permissions not found"));
    }
}