package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class ShowPhrase extends Command {

    public ShowPhrase() {
        super("sp", "/sp [phrase]", "показывает, какой кластер обрабатывает данную фразу");
    }

    @Override
    public void handle(String[] args) {
        if(args.length == 0) {
            notEnoughArgs();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; ++i)
            sb.append(args[i]).append(" ");
        String msg = sb.toString().trim();
        String cluster = DarlingKate.getClassifier().getClusterName(msg);
        if(cluster == null)
            error("Эта фраза не обрабатывается ни одним из кластеров.");
        else
            sender.sendMessage("Указанную фразу обрабатывает кластер с названием '%s'.", cluster);
    }

}
