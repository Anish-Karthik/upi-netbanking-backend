package site.anish_karthik.upi_net_banking.server.utils;

import site.anish_karthik.upi_net_banking.server.model.Permission;

public class PermissionUtil {
    public static void comparePermissions(Permission permission, Permission requiredPermission, String paymentMethod) throws Exception {
        checkPermission(permission.getCanDeposit(), requiredPermission.getCanDeposit(), "deposit", paymentMethod);
        checkPermission(permission.getCanWithdraw(), requiredPermission.getCanWithdraw(), "withdraw", paymentMethod);
        checkPermission(permission.getCanSendMoney(), requiredPermission.getCanSendMoney(), "send money", paymentMethod);
        checkPermission(permission.getCanReceiveMoney(), requiredPermission.getCanReceiveMoney(), "receive money", paymentMethod);
    }

    public static void checkPermission(boolean hasPermission, boolean requiresPermission, String action, String paymentMethod) throws Exception {
        if (requiresPermission && !hasPermission) {
            throw new Exception("Permission denied: Cannot " + action + " using " + paymentMethod);
        }
    }
}
