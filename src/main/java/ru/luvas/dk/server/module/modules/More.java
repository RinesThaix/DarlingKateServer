package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.IterableModule;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.user.Session;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class More extends Module {
    
    public final static String KEY_ITERABLE_MODULE = "iterable_module";

    public More() {
        super(Lists.newArrayList("еще", "следующий", "следующая", "следующее", "далее", "дальше"));
    }

    @Override
    public RequestResult handle(Session session, String msg) {
        if(!session.has(KEY_ITERABLE_MODULE))
            return new RequestResult("Не поняла тебя.");
        IterableModule module = (IterableModule) session.get(KEY_ITERABLE_MODULE);
        return module.next(session);
    }

}
