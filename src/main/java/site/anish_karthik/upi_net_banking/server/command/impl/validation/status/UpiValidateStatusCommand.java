package site.anish_karthik.upi_net_banking.server.command.impl.validation.status;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.dto.GetUpiDTO;
import site.anish_karthik.upi_net_banking.server.model.enums.UpiStatus;
import site.anish_karthik.upi_net_banking.server.service.UpiService;

public class UpiValidateStatusCommand implements Command {
    private final String upiId;
    private final UpiService upiService;

    public UpiValidateStatusCommand(String upiId, UpiService upiService) {
        this.upiId = upiId;
        this.upiService = upiService;
    }

    @Override
    public void execute() throws Exception {
        GetUpiDTO upi = upiService.getUpiById(upiId);
        if (upi == null) {
            throw new Exception("UPI ID not found");
        }
        if (!upi.getStatus().equals(UpiStatus.ACTIVE)) {
            throw new Exception("UPI ID is not active");
        }
    }

    @Override
    public void undo() throws Exception {

    }
}
