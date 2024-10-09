package site.anish_karthik.upi_net_banking.server.command;

// TODO: Implement the interface segregating the undo functionality
public interface UndoAbleCommand extends Command{
    void undo() throws Exception;
}
