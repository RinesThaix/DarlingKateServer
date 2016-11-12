package ru.luvas.dk.server.event;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public abstract class Event {
    
    public Event call() {
        return EventManager.call(this);
    }
    
}
