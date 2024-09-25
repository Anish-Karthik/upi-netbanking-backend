package site.anish_karthik.upi_net_banking.server.strategies;

import site.anish_karthik.upi_net_banking.server.model.Permission;

// Concrete Strategies
public class RecieveMoneyPermissionStrategy implements PermissionStrategy {
    private final Permission permission;

    public RecieveMoneyPermissionStrategy(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean canPerformAction() {
        return permission.isCanReceiveMoney();
    }
}
