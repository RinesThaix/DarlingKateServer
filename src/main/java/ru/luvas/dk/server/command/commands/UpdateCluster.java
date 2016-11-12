package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class UpdateCluster extends Command {

    public UpdateCluster() {
        super("uc", "/uc [cluster name] [message]", "создать/обновить кластер, добавив в него ответ");
    }

    @Override
    public void handle(String[] args) {
        if(args.length < 2) {
            notEnoughArgs();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length; ++i)
            sb.append(args[i]).append(" ");
        DarlingKate.getClassifier().updateCluster(args[0], sb.toString().trim());
        sender.sendMessage("Кластер %s обновлен.", args[0]);
    }

}
