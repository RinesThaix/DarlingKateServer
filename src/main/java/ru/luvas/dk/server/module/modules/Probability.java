package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.util.UtilAlgo;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Probability extends Module {

    public Probability() {
        super(Lists.newArrayList("инфа", "рандом", "случайность", "вероятность", "шанс"));
    }

    @Override
    public RequestResult handle(String msg) {
        int r = UtilAlgo.r(101);
        return new RequestResult("Мой вердикт: " + r + " " + getPercentageName(r), "Мой вердикт: " + r + "%", null);
    }
    
    private String getPercentageName(int v) {
        int o1 = v % 10, o2 = v % 100;
        if(o1 == 1 && o2 != 11)
            return "процент";
        if(o1 >= 2 && o1 <= 4 && (o2 < 10 || o2 > 20))
            return "процента";
        return "процентов";
    }

}
