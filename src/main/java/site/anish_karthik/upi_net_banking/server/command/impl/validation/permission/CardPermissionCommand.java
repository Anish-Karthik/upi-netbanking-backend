package site.anish_karthik.upi_net_banking.server.command.impl.validation.permission;


import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.service.PermissionService;
import site.anish_karthik.upi_net_banking.server.service.impl.PermissionServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.PermissionUtil;

public class CardPermissionCommand implements Command {
    private final Permission requiredPermission;
    private final String cardNo;
    private final PermissionService permissionService;

    public CardPermissionCommand(String cardNo, Permission requiredPermission) {
        this.cardNo = cardNo;
        this.requiredPermission = requiredPermission;
        this.permissionService = new PermissionServiceImpl();
    }

    @Override
    public void execute() throws Exception {
        Permission permission = permissionService.getPermissionByCardNo(cardNo);
        PermissionUtil.comparePermissions(permission, requiredPermission, "CARD - "+ cardNo);
    }

    @Override
    public void undo() throws Exception {

    }
}
