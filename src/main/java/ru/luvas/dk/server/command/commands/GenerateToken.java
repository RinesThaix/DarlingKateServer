package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.command.Command;
import ru.luvas.dk.server.spring.Authenticator;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class GenerateToken extends Command {

    public GenerateToken() {
        super("gentoken", "/gentoken", "создает валидный токен");
    }

    @Override
    public void handle(String[] args) {
        sender.sendMessage("Ваш токен: " + Authenticator.generateToken());
    }

}
