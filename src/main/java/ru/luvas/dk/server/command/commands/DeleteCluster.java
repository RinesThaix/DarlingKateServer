package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class DeleteCluster extends Command {

    public DeleteCluster() {
        super("dc", "/dc [cluster name]", "удалить указанный кластер ответов");
    }

    @Override
    public void handle(String[] args) {
        if(args.length != 1) {
            notEnoughArgs();
            return;
        }
        DarlingKate.getClassifier().removeCluster(args[0]);
        sender.sendMessage("Кластер %s удален.", args[0]);
    }

}
