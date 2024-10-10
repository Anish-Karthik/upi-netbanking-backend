package site.anish_karthik.upi_net_banking.server.command.invoker;

import lombok.Getter;
import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
public class TransferInvoker implements Invoker<Command> {
    private final List<Command> commands = new ArrayList<>();

    @Override
    public void addCommand(Command command) {
        System.out.println("HEY");
        this.commands.add( command);
    }

    @Override
    public void executeSerially() throws Exception {
        for (Command command : this.commands) {
            try {
                command.execute();
            } catch (Exception e) {
                System.err.println("Command execution failed: " + e.getMessage());
                undoCommands();
                throw e;
            }
        }
        commands.clear();
    }

    @Override
    public void executeInParallel() throws Exception {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Command command : this.commands) {
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    command.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        commands.clear();
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Command execution failed: " + e.getMessage());
            undoCommands();
            throw e;
        }
    }

    public void undoCommands() throws Exception {
        for (int i = this.commands.size() - 1; i >= 0; i--) {
            this.commands.get(i).undo();
        }
    }
}