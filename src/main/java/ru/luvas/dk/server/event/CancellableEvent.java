package ru.luvas.dk.server.event;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public abstract class CancellableEvent extends Event {

    @Setter
    @Getter
    private boolean cancelled = false;
    
}