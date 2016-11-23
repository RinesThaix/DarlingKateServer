package ru.luvas.dk.server.event.listener;

import com.google.common.collect.Lists;
import java.util.List;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.command.CommandManager;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.event.Listener;
import ru.luvas.dk.server.event.ListenerPriority;
import ru.luvas.dk.server.event.events.ChatCommandEvent;
import ru.luvas.dk.server.event.events.ChatEvent;
import ru.luvas.dk.server.event.events.RequestEvent;
import ru.luvas.dk.server.module.ModuleManager;
import ru.luvas.dk.server.user.Console;
import ru.luvas.dk.server.user.Session;
import ru.luvas.dk.server.util.Pair;
import ru.luvas.dk.server.util.Rand;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class SystemListener {

    @Listener(priority = ListenerPriority.SYSTEM)
    public void onChat(ChatEvent e) {
        if(e.isCancelled())
            return;
        String msg = e.getMessage();
        if(msg == null)
            return;
        if(msg.length() > 1) {
            if(msg.startsWith("/")) {
                String cmd = msg.substring(1);
                String[] pargs = cmd.split(" ");
                cmd = pargs[0];
                String[] args = new String[pargs.length - 1];
                for(int i = 1; i < pargs.length; ++i)
                    args[i - 1] = pargs[i];
                new ChatCommandEvent(cmd, args).call();
                return;
            }
        }
        RequestEvent reqEvent = new RequestEvent(Session.getConsoleSession(), msg);
        reqEvent.call();
        RequestResult result = reqEvent.getResult();
        String speech = result.getSpeech(), message = result.getMessage();
        if(speech.equals(message))
            Console.getInstance().sendMessage(speech);
        else
            Console.getInstance().sendMessage(Lists.newArrayList(speech, message));
    }
    
    @Listener(priority = ListenerPriority.SYSTEM)
    public void onCommand(ChatCommandEvent e) {
        if(e.isCancelled())
            return;
        CommandManager.handle(e.getCommand(), e.getArgs());
    }
    
    private final static List<String> unknown = Lists.newArrayList(
            "Прости, но я не могу понять тебя.",
            "Ты не мог бы повторить это еще раз?",
            "Не понимаю, чего ты от меня хочешь.",
            "Я не могу осознать, что тебе нужно."
    );
    
    @Listener(priority = ListenerPriority.SYSTEM)
    public void onRequest(RequestEvent e) {
        if(e.isCancelled())
            return;
        String msg = e.getMessage();
        Pair<Boolean, RequestResult> resultingPair = ModuleManager.handle(e.getSession(), msg);
        if(resultingPair.getFirst()) {
            e.setResult(resultingPair.getSecond());
            return;
        }
        String answer = DarlingKate.getClassifier().classify(msg);
        if(answer == null)
            answer = Rand.of(unknown);
        else if((resultingPair = ModuleManager.handle(e.getSession(), answer)).getFirst()) {
            e.setResult(resultingPair.getSecond());
            return;
        }
        e.setResult(new RequestResult(answer));
    }
    
}