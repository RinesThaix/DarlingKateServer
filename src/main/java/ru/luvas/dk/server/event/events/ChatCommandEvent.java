package ru.luvas.dk.server.event.events;

import lombok.Data;
import ru.luvas.dk.server.event.CancellableEvent;
import ru.luvas.dk.server.user.Console;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class ChatCommandEvent extends CancellableEvent {
    
    private final static Console sender = Console.getInstance();
    private final String command;
    private final String[] args;

}