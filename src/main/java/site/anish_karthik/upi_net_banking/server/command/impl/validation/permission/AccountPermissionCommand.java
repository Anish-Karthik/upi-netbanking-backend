package site.anish_karthik.upi_net_banking.server.command.impl.validation.permission;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.service.PermissionService;
import site.anish_karthik.upi_net_banking.server.service.impl.PermissionServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.PermissionUtil;

public class AccountPermissionCommand implements Command {
    private final Permission requiredPermission;
    private final String accNo;
    private final PermissionService permissionService;

    public AccountPermissionCommand(String accNo, Permission requiredPermission) {
        this.accNo = accNo;
        this.requiredPermission = requiredPermission;
        this.permissionService = new PermissionServiceImpl();
    }

    @Override
    public void execute() throws Exception {
        Permission permission = permissionService.getPermissionByAccNo(accNo);
        PermissionUtil.comparePermissions(permission, requiredPermission, "Account - "+ accNo);
    }

    @Override
    public void undo() throws Exception {

    }
}