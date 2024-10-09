package site.anish_karthik.upi_net_banking.server.utils;

import site.anish_karthik.upi_net_banking.server.model.Permission;

public class PermissionUtil {
    public static void comparePermissions(Permission permission, Permission requiredPermission, String paymentMethod) throws Exception {
        checkPermission(permission.isCanDeposit(), requiredPermission.isCanDeposit(), "deposit", paymentMethod);
        checkPermission(permission.isCanWithdraw(), requiredPermission.isCanWithdraw(), "withdraw", paymentMethod);
        checkPermission(permission.isCanSendMoney(), requiredPermission.isCanSendMoney(), "send money", paymentMethod);
        checkPermission(permission.isCanReceiveMoney(), requiredPermission.isCanReceiveMoney(), "receive money", paymentMethod);
    }

    public static void checkPermission(boolean hasPermission, boolean requiresPermission, String action, String paymentMethod) throws Exception {
        if (requiresPermission && !hasPermission) {
            throw new Exception("Permission denied: Cannot " + action + " using " + paymentMethod);
        }
    }
}
