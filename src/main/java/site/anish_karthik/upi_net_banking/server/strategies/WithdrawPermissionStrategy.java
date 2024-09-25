package site.anish_karthik.upi_net_banking.server.strategies;

import site.anish_karthik.upi_net_banking.server.model.Permission;

public class WithdrawPermissionStrategy implements PermissionStrategy {
    private final Permission permission;

    public WithdrawPermissionStrategy(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean canPerformAction() {
        return permission.isCanWithdraw();
    }
}
