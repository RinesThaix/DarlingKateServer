package ru.luvas.dk.server.command.commands;

import ru.luvas.dk.server.command.Command;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.event.events.RequestEvent;
import ru.luvas.dk.server.spring.Errors;
import ru.luvas.dk.server.util.Logger;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Json extends Command {

    public Json() {
        super("json", "/json [message]", "выводит json-ответ на переданное сообщение");
    }

    @Override
    public void handle(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String s : args)
            sb.append(s);
        String msg = sb.toString();
        if(msg.isEmpty()) {
            error("Для этой команды обязательно указывание сообщения.");
            return;
        }
        if(msg.length() > 64) {
            sender.sendMessage(Errors.TOO_LONG_MESSAGE.toJson());
            return;
        }
        try {
            RequestEvent reqEvent = new RequestEvent(msg);
            reqEvent.call();
            if(reqEvent.isCancelled()) {
                sender.sendMessage(Errors.REQUEST_WAS_DENIED.toJson());
                return;
            }
            RequestResult result = reqEvent.getResult();
            if(result == null) {
                sender.sendMessage(Errors.CAN_NOT_HANDLE_REQUEST.toJson());
                return;
            }
            sender.sendMessage(result.toJson());
        }catch(Exception ex) {
            Logger.warn(ex, "We had caught an error whilst performing a /json command");
            sender.sendMessage(Errors.UNEXPECTED_ERROR.toJson());
        }
    }

}
