// Command.java
package site.anish_karthik.upi_net_banking.server.command;

public interface Command extends GeneralCommand {
    void undo() throws Exception;
}

