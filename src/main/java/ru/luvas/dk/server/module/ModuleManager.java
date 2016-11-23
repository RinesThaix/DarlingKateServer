package ru.luvas.dk.server.module;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.modules.*;
import ru.luvas.dk.server.user.Session;
import ru.luvas.dk.server.util.Logger;
import ru.luvas.dk.server.util.Pair;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class ModuleManager {
    
    @Getter
    private final static Set<Module> modules = new HashSet<>();
    
    public static void init() {
        load(new Probability());
        load(new Version());
        load(new Kitty());
        load(new Schedule());
        load(new More());
        load(new News());
        
        Logger.log("Registered %d modules in total.", modules.size());
    }
    
    private static void load(Module module) {
        modules.add(module);
    }
    
    private final static Pair<Boolean, RequestResult> nope = new Pair<>(false, null);
    
    /**
     * Tries to find module for the given message & to execute it.
     * @param session user's session.
     * @param msg the message.
     * @return Pair(false, null) whether there's no such a module or Pair(true, RequestResult), whether
     * we succeed with our task.
     */
    public static Pair<Boolean, RequestResult> handle(Session session, String msg) {
        String lowered = msg.toLowerCase();
        for(Module module : modules) {
            for(String prefix : module.getPrefixes())
                if(lowered.startsWith(prefix)) {
                    if(!(module instanceof IterableModule) && !(module instanceof More))
                        session.remove(More.KEY_ITERABLE_MODULE);
                    return new Pair<>(true, module.handle0(session, msg.substring(prefix.length())));
                }
        }
        return nope;
    }
    
    public static boolean contains(String prefix) {
        prefix = prefix.toLowerCase();
        for(Module module : modules)
            for(String p : module.getPrefixes())
                if(prefix.startsWith(p))
                    return true;
        return false;
    }
}
