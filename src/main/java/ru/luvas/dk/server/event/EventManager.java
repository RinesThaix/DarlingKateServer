package ru.luvas.dk.server.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class EventManager {

    private final static Map<Class<? extends Event>, TreeSet<ListeningMethod>> handlers = new HashMap<>();
    
    public static synchronized void register(Object handler) {
        Class<Listener> listener = Listener.class;
        Class event = Event.class;
        for(Method m : handler.getClass().getMethods()) {
            Listener annotation = m.getAnnotation(listener);
            if(annotation == null)
                continue;
            Class[] params = m.getParameterTypes();
            if(params.length != 1 || !event.isAssignableFrom(params[0]))
                continue;
            addHandler(params[0], handler, m, annotation.priority());
        }
    }
    
    private static synchronized void addHandler(Class<? extends Event> event, Object handler, Method method, ListenerPriority priority) {
        TreeSet<ListeningMethod> methods = handlers.get(event);
        if(methods == null) {
            methods = new TreeSet<>((a, b) -> a.priority.ordinal() - b.priority.ordinal());
            handlers.put(event, methods);
        }
        methods.add(new ListeningMethod(handler, method, priority));
    }
    
    public static <T extends Event> T call(T event) {
        return handle(event);
    }
    
    public static synchronized <T extends Event> T handle(T event) {
        Set<ListeningMethod> methods = handlers.get(event.getClass());
        if(methods == null)
            return event;
        try {
            for(ListeningMethod lm : methods)
                lm.method.invoke(lm.handler, event);
        }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return event;
    }
    
    @RequiredArgsConstructor
    private static class ListeningMethod {
        
        private final Object handler;
        private final Method method;
        private final ListenerPriority priority;
        
    }
    
}
