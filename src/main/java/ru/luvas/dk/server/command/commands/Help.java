package ru.luvas.dk.server.command.commands;

import java.util.ArrayList;
import java.util.List;
import ru.luvas.dk.server.command.Command;
import ru.luvas.dk.server.command.CommandManager;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Help extends Command {

    public Help() {
        super("help", "/help", "справка помощи по серверным командам");
    }

    @Override
    public void handle(String[] args) {
        List<String> answer = new ArrayList<>();
        answer.add("Доступные команды:");
        CommandManager.getCommands().values().stream().filter(c -> !c.isDisabled())
                .forEach(c -> answer.add(String.format("%s - %s.", c.getUsage(), c.getDescription())));
        sender.sendMessage(answer);
    }

}
