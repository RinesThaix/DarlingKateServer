package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.custom.RequestResultSpeak;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.user.Session;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Version extends Module {

    public Version() {
        super(Lists.newArrayList("версия", "твоя версия", "выведи версию"));
    }

    @Override
    public RequestResult handle(Session session, String msg) {
        String commitId = DarlingKate.getInstance().getCommitId();
        if(commitId.equals("unknown"))
            return new RequestResultSpeak("В данный момент я работаю под неизвестной версией своего кода.");
        String subversion = commitId.substring(0, 7).toUpperCase();
        String lastUpdate = DarlingKate.getInstance().getCommitDate();
        return new RequestResultSpeak("Я скинула информацию о своей версии в текстовом сообщении.",
                "Версия: #" + subversion + "\n" +
                "Последнее обновление: " + lastUpdate,
                null);
    }

}