package ru.luvas.dk.server.command;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import ru.luvas.dk.server.command.commands.*;
import ru.luvas.dk.server.user.Console;
import ru.luvas.dk.server.util.Logger;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class CommandManager {
    
    @Getter
    private final static Map<String, Command> commands = new HashMap<>();
    
    public static void init() {
        load(new Help());
        load(new Stop());
        load(new ShowCluster());
        load(new ShowPhrase());
        load(new UpdateCluster());
        load(new UpdatePhrase());
        load(new DeleteCluster());
        load(new DeletePhrase());
        load(new NeuralReload());
        load(new ListClusters());
        
        Logger.log("Registered %d commands in total.", commands.size());
    }
    
    private static void load(Command cmd) {
        commands.put(cmd.getName().toLowerCase(), cmd);
    }
    
    public static void handle(String cmd, String[] args) {
        Command command = commands.get(cmd.toLowerCase());
        if(command == null) {
            Console.getInstance().sendMessage("Неизвестная команда!");
            return;
        }
        command.handle0(args);
    }
    
    public static boolean contains(String cmd) {
        return commands.containsKey(cmd.toLowerCase());
    }

}