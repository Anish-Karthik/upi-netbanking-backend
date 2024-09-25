package site.anish_karthik.upi_net_banking.server.strategies;


import site.anish_karthik.upi_net_banking.server.model.Permission;

public class WithdrawPermissionStrategy implements PermissionStrategy {
    private final Permission permissions;

    public WithdrawPermissionStrategy(Permission permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean canPerformAction() {
        return permissions.getCanWithdraw();
    }
}
