package site.anish_karthik.upi_net_banking.server.strategies;

import site.anish_karthik.upi_net_banking.server.model.Permission;

// Concrete Strategies
public class RecieveMoneyPermissionStrategy implements PermissionStrategy {     private final Permission permissions;

    public RecieveMoneyPermissionStrategy(Permission permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean canPerformAction() {
        return permissions.getCanReceiveMoney();
    }
}

