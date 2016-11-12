package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class UpdatePhrase extends Command {

    public UpdatePhrase() {
        super("up", "/up [phrase] [cluster name]", "сообщить серверу, что на указанную фразу нужно отвечать сообщениями из указанного кластера");
    }

    @Override
    public void handle(String[] args) {
        if(args.length < 2) {
            notEnoughArgs();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length - 1; ++i)
            sb.append(args[i]).append(" ");
        String msg = sb.toString().trim();
        DarlingKate.getClassifier().learn(msg, args[args.length - 1]);
        sender.sendMessage("Ответ на фразу '%s' добавлен в виде кластера %s.", msg, args[args.length - 1]);
    }

}
