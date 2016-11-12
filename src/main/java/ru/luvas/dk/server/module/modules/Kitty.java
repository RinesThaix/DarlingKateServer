package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.util.Logger;
import ru.luvas.dk.server.util.PostExecutor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Kitty extends Module {
    
    private final static String URL = "http://thecatapi.com/api/images/get?format=xml&results_per_page=1&type=png&size=med";

    public Kitty() {
        super(Lists.newArrayList("котик", "киса", "киска", "котенок", "покажи котика", "покажи котенка", "покажи киску", "покажи кису"));
    }

    @Override
    public RequestResult handle(String msg) {
        try {
            @SuppressWarnings("deprecation")
            String xml = PostExecutor.executeGet(URL);
            xml = xml.split("<url>")[1].split("</url>")[0];
            return new RequestResult("Котенок отправлен!", "Вот, держи:", xml);
        }catch(Exception ex) {
            ex.printStackTrace();
            return new RequestResult(null);
        }
    }

}