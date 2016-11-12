package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class DeletePhrase extends Command {

    public DeletePhrase() {
        super("dp", "/dp [phrase]", "очистить указанную фразу от кластеров");
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
        DarlingKate.getClassifier().removePhrase(msg);
        sender.sendMessage("Фраза '%s' успешно очищена от кластеров.", msg);
    }

}
