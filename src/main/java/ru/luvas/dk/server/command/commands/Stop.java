package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Stop extends Command {

    public Stop() {
        super("stop", "/stop", "остановить сервер");
    }

    @Override
    public void handle(String[] args) {
        sender.sendMessage("Выключаюсь..");
        DarlingKate.getInstance().disable();
    }

}
