package ru.luvas.dk.server.module;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.modules.*;
import ru.luvas.dk.server.util.Logger;

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
        
        Logger.log("Registered %d modules in total.", modules.size());
    }
    
    private static void load(Module module) {
        modules.add(module);
    }
    
    public static RequestResult handle(String msg) {
        String lowered = msg.toLowerCase();
        for(Module module : modules) {
            for(String prefix : module.getPrefixes())
                if(lowered.startsWith(prefix))
                    return module.handle0(msg.substring(prefix.length()));
        }
        return null;
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
