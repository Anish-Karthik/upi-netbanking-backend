package site.anish_karthik.upi_net_banking.server.strategies;

import site.anish_karthik.upi_net_banking.server.model.Permission;

// Concrete Strategies
public class SendMoneyPermissionStrategy implements PermissionStrategy {
    private final Permission permissions;

    public SendMoneyPermissionStrategy(Permission permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean canPerformAction() {
        return permissions.getCanSendMoney();
    }
}
