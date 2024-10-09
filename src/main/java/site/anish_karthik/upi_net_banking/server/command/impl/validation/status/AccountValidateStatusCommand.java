package site.anish_karthik.upi_net_banking.server.command.impl.validation.status;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

public class AccountValidateStatusCommand implements Command {
    private final String accNo;
    private final BankAccountService accountService;

    public AccountValidateStatusCommand(String accNo, BankAccountService accountService) {
        this.accNo = accNo;
        this.accountService = accountService;
    }

    @Override
    public void execute() throws Exception {
        BankAccount acc = accountService.getBankAccountByAccNo(accNo);
        if (acc == null) {
            throw new Exception("Acc No. not found");
        }
        if (!acc.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new Exception("Acc No. is not active");
        }
    }

    @Override
    public void undo() throws Exception {

    }
}

