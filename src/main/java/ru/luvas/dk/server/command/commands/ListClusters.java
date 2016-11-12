package ru.luvas.dk.server.command.commands;

import java.util.ArrayList;
import java.util.List;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class ListClusters extends Command {

    public ListClusters() {
        super("lc", "/lc <page>", "выводит список всех кластеров");
    }

    @Override
    public void handle(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Все существующие кластеры:");
        int page = 1;
        if(args.length == 1)
            try {
                page = Integer.parseInt(args[0]);
            }catch(NumberFormatException ex) {}
        List<String> clusters = new ArrayList<>(DarlingKate.getClassifier().getAllClustersNames());
        final int perPage = 20;
        for(int i = (page - 1) * perPage; i < page * perPage; ++i)
            list.add("- " + clusters.get(i));
        sender.sendMessage(list);
    }

}
