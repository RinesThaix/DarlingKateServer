package ru.luvas.dk.server.command;

import com.google.common.collect.Lists;
import lombok.Data;
import ru.luvas.dk.server.user.Console;
import ru.luvas.dk.server.util.Logger;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public abstract class Command {
    
    protected final static Console sender = Console.getInstance();

    private final String name;
    
    private final String usage;
    
    private final String description;
    
    private boolean disabled = false;
    
    void handle0(String[] args) {
        if(disabled) {
            sender.sendMessage("Эта команда временно отключена.");
            return;
        }
        handle(args);
    }
    
    public abstract void handle(String[] args);
    
    protected void notEnoughArgs() {
        sender.sendMessage(Lists.newArrayList(
                "Неверное число аргументов для команды!",
                "Правильное использование: %s"
        ), usage);
    }
    
    protected void error(String msg) {
        sender.sendMessage(Lists.newArrayList("Ошибка!", msg));
    }
    
    protected void error(String msg, Object... args) {
        error(String.format(msg, args));
    }
    
    protected void fatal() {
        Logger.warn("Fatal error handling %s!", getName());
    }
    
    protected void fatal(Exception ex) {
        Logger.warn(ex, "&4Fatal error handling &e" + getName() + "&4!");
    }
    
}