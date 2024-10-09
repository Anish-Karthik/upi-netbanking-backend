package site.anish_karthik.upi_net_banking.server.command.impl.validation.permission;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.service.PermissionService;
import site.anish_karthik.upi_net_banking.server.service.impl.PermissionServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.PermissionUtil;

public class UpiPermissionCommand implements Command {
    private final Permission requiredPermission;
    private final String upiId;
    private final PermissionService permissionService;

    public UpiPermissionCommand(String upiId, Permission requiredPermission) {
        this.upiId = upiId;
        this.requiredPermission = requiredPermission;
        this.permissionService = new PermissionServiceImpl();
    }

    @Override
    public void execute() throws Exception {
        Permission permission = permissionService.getPermissionByUpiId(upiId);
        PermissionUtil.comparePermissions(permission, requiredPermission, "UPI - "+ upiId);
    }

    @Override
    public void undo() throws Exception {

    }
}
