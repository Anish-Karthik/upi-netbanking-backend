package site.anish_karthik.upi_net_banking.server.strategy;

import site.anish_karthik.upi_net_banking.server.model.Permission;

public class WithdrawPermissionStrategy implements PermissionStrategy {
    private final Permission permission;

    public WithdrawPermissionStrategy(Permission permission) {
        this.permission = permission;
    }

    @Override
    public Boolean canPerformAction() {
        return permission.getCanWithdraw();
    }
}
