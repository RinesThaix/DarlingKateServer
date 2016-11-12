package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.Command;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class NeuralReload extends Command {

    public NeuralReload() {
        super("neuralreload", "/neuralreload", "перезагрузка нейронной сети");
    }

    @Override
    public void handle(String[] args) {
        sender.sendMessage("Перезагружаем нейронную сеть, обновляя последние полученные данные..");
        DarlingKate.getInstance().reloadClassifier();
        DarlingKate.getClassifier().classify("привет"); //to preload SMO
        sender.sendMessage("Нейронная сеть успешно перезагружена.");
    }

}
