package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import java.util.List;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.util.UtilAlgo;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Probability extends Module {

    public Probability() {
        super(Lists.newArrayList("рандом", "случайность", "вероятность", "шанс"));
    }

    @Override
    public RequestResult handle(String msg) {
        int r = UtilAlgo.r(101);
        return new RequestResult("Мой вердикт: " + r + " процентов", "Мой вердикт: " + r + "%", null);
    }

}
