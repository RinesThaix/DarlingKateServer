package ru.luvas.dk.server.module;

import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.user.Session;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public interface IterableModule {

    RequestResult next(Session session);
    
}
