package ru.luvas.dk.server.command.commands;

import java.util.ArrayList;
import java.util.List;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class ShowCluster extends Command {

    public ShowCluster() {
        super("sc", "/sc [cluster name]", "показывает возможные ответы указанного кластера");
    }

    @Override
    public void handle(String[] args) {
        if(args.length != 1) {
            notEnoughArgs();
            return;
        }
        List<String> answers = DarlingKate.getClassifier().getFullCluster(args[0]);
        if(answers.isEmpty())
            error("Кластера с указанным названием не существует.");
        else {
            List<String> list = new ArrayList<>();
            list.add("Содержимое кластера %s:");
            answers.stream().map(s -> "- " + s).forEach(list::add);
            sender.sendMessage(list, args[0]);
        }
    }

}
