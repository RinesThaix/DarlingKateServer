package ru.luvas.dk.server.module;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.custom.RequestResultNotify;
import ru.luvas.dk.server.spring.Errors;
import ru.luvas.dk.server.user.Session;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public abstract class Module {

    /**
     * Список префиксов, наличие которых в начале сообщения будет приводить
     * к исполнению этого самого модуля.
     */
    @Getter
    private final List<String> prefixes;
    
    @Setter
    @Getter
    private boolean disabled = false;
    
    public Module(List<String> prefixes) {
        this.prefixes = prefixes;
        this.prefixes.sort((a, b) -> b.length() - a.length());
    }
    
    RequestResult handle0(Session session, String msg) {
        if(disabled)
            return new RequestResult(Errors.MODULE_TEMPORARILY_DISABLED);
        return handle(session, msg);
    }
    
    public abstract RequestResult handle(Session session, String msg);
    
    protected RequestResult notEnoughArgs() {
        return new RequestResultNotify("Неверное число аргументов!", "Неверное число аргументов для команды!");
    }
    
}
