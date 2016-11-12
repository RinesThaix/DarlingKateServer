package ru.luvas.dk.server.command.commands;

import org.json.simple.JSONObject;
import ru.luvas.dk.server.command.Command;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.event.events.RequestEvent;
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
            sender.sendMessage(getError("This message is too long."));
            return;
        }
        try {
            RequestEvent reqEvent = new RequestEvent(msg);
            reqEvent.call();
            if(reqEvent.isCancelled()) {
                sender.sendMessage(getError("By some reason this request was denied."));
                return;
            }
            RequestResult result = reqEvent.getResult();
            if(result == null || result.getMessage() == null) {
                sender.sendMessage(getError("By some reason we are unable to handle your request."));
                return;
            }
            sender.sendMessage(result.toJson());
        }catch(Exception ex) {
            Logger.warn(ex, "We had caught an error whilst performing a /json command");
            sender.sendMessage(getError());
        }
    }
    
    private String getError() {
        return getError("Unexpected error occured whilst trying to handle your request.");
    }
    
    private String getError(String message) {
        JSONObject json = new JSONObject();
        json.put("error", true);
        json.put("text", message);
        return json.toJSONString();
    }

}
