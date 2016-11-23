package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.Module;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Schedule extends Module {
    
    private final static Map<Character, Character> phoneticReplaces = new HashMap<Character, Character>() {{
        put('м', 'M');
        put('а', 'A');
        put('у', 'U');
        put('с', 'S');
        put('е', 'E');
        put('к', 'K');
    }};

    public Schedule() {
        super(Lists.newArrayList("расписание", "покажи расписание", "подскажи расписание"));
    }

    @Override
    public RequestResult handle(String msg) {
        msg = msg.toLowerCase();
        String[] spl = msg.split(" ");
        StringBuilder sb = new StringBuilder();
        msg = msg.replace(" ", "");
        if(msg.length() < 5)
            return new RequestResult("Я не поняла, информацию по какой группе ты хочешь получить.");
        for(int i = msg.length() - 1; i >= msg.length() - 4; --i)
            sb.append(msg.charAt(i));
        String group = spl[spl.length - 2].charAt(0) + sb.reverse().toString();
        for(Entry<Character, Character> entry : phoneticReplaces.entrySet())
            group = group.replace(entry.getKey(), entry.getValue());
        char[] chars = group.toCharArray();
        boolean valid = true;
        if(chars[0] < 'A' || chars[0] > 'Z')
            valid = false;
        for(int i = 1; i < chars.length; ++i)
            if(chars[i] < '0' || chars[i] > '9') {
                valid = false;
                break;
            }
        if(!valid)
            return new RequestResult("Названная тобой группа невалидна. Не уверена, что я правильно услышала тебя, "
                    + "так что отправила ее название в текстовом сообщении.",
                    "Ты спросил про группу " + group + "?\nПопробуй сказать 'расписание для группы Москва 3236 (М3236)' или 'расписание Астрахань 3201 (A3201)'", null);
        @SuppressWarnings("deprecation")
        int currentDay = new Date().getDay();
        if(currentDay == 0)
            return new RequestResult("В воскресенье нет пар, дурачок!");
        String url = "http://www.ifmo.ru/ru/schedule/0/" + group + "/raspisanie_zanyatiy_" + group + ".htm";
        List<String> first = new ArrayList<>(),
                second = new ArrayList<>(),
                third = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements all = doc.select("div.container").select("div.page-content").select("div.rasp_tabl_day")
                    .select("table.rasp_tabl");
            int id = 0;
            for(Element table : all) {
                if(++id != currentDay)
                    continue;
                for(Element pair : table.select("td.time")) {
                    String time = pair.select("span").first().text();
                    String ned = pair.select("dt").first().text();
                    if(ned.startsWith("неч"))
                        ned = "неч.";
                    else if(ned.startsWith("чет"))
                        ned = "чет.";
                    time += " (" + (ned.isEmpty() ? "всегда" : ned) + ") - ";
                    first.add(time);
                }
                for(Element pair : table.select("td.room"))
                    second.add(pair.select("span").first().text().replace("ул.", "").replace("д.", "") + ", " +
                            pair.select("dd").first().text() + " - ");
                for(Element pair : table.select("td.lesson"))
                    third.add(pair.select("dd").first().text() + ".");
            }
            if(first.isEmpty())
                return new RequestResult("Расписание для этой группы не найдено. Не уверена, что расслышала группу правильно, так что отправляю ее в текстовом сообщении.",
                        "Ты спросил про группу " + group + "?", null);
            sb = new StringBuilder();
            sb.append("Расписание для группы ").append(group).append(":\n");
            for(int i = 0; i < first.size(); ++i)
                sb.append(first.get(i)).append(second.get(i)).append(third.get(i)).append("\n");
            return new RequestResult("Я выслала сегодняшнее расписание для этой группы в текстовом сообщении.", sb.toString(), null);
        }catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
