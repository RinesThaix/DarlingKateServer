package ru.luvas.dk.server.event.events;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.luvas.dk.server.custom.Location;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.event.CancellableEvent;
import ru.luvas.dk.server.user.Session;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
@RequiredArgsConstructor
public class RequestEvent extends CancellableEvent {
    
    private final Session session;
    
    private final String message;
    
    private final Location location;
    
    private RequestResult result;
    
    public RequestEvent(Session session, String message) {
        this(session, message, null);
    }

}
