package site.anish_karthik.upi_net_banking.server.strategy;

import site.anish_karthik.upi_net_banking.server.model.Permission;

// Concrete Strategies
public class SendMoneyPermissionStrategy implements PermissionStrategy {
    private final Permission permission;

    public SendMoneyPermissionStrategy(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean canPerformAction() {
        return permission.isCanSendMoney();
    }
}
