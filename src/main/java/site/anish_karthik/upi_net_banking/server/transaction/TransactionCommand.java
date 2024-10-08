// TransactionCommand.java
package site.anish_karthik.upi_net_banking.server.transaction;

public interface TransactionCommand {
    void execute() throws Exception;
    void rollback() throws Exception;
}