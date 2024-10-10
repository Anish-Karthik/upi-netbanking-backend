package site.anish_karthik.upi_net_banking.server.command.invoker;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;

public interface Invoker<T> {
    void addCommand(T command);
    void executeSerially() throws Exception;
    void executeInParallel() throws Exception;
}
