package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.json.simple.JSONObject;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.module.IterableModule;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.user.Session;
import ru.luvas.dk.server.util.PostExecutor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class News extends Module implements IterableModule {
    
    private final static String KEY_NEWS_ITERATOR = "news_iterator";
    private final static int NEWS_SIZE = 50;
    
    private final List<NewsItem> news = new ArrayList<>();
    private long lastUpdate = 0l;

    public News() {
        super(Lists.newArrayList("новости", "покажи новости", "выведи новости", "последние новости"));
    }

    @Override
    public RequestResult handle(Session session, String msg) {
        checkForUpdates();
        if(news.isEmpty())
            return new RequestResult("В данный момент я не могу получить список последних новостей.");
        session.set(More.KEY_ITERABLE_MODULE, this);
        session.set(KEY_NEWS_ITERATOR, 0);
        return new RequestResult(news.get(0).getTitle());
    }

    @Override
    public RequestResult next(Session session) {
        if(checkForUpdates()) {
            session.set(KEY_NEWS_ITERATOR, -1);
            return new RequestResult("Буквально только что я запросила новую информацию о новостях, поэтому начнем сначала. Скажи еще раз дальше.");
        }
        int index = session.getInt(KEY_NEWS_ITERATOR);
        if(index >= news.size())
            return new RequestResult("Мы с тобой дошли до конца списка последних новостей. Такие вот дела.");
        String news = this.news.get(++index).getTitle();
        session.set(KEY_NEWS_ITERATOR, index);
        return new RequestResult(news);
    }
    
    private boolean checkForUpdates() {
        long current = System.currentTimeMillis();
        if(current - lastUpdate > 1000l * 60 * 20) {
            lastUpdate = current;
            news.clear();
            String url = "https://meduza.io/api/v3/search?chrono=news&page=0&per_page=" + NEWS_SIZE + "&locale=ru";
            try {
                JSONObject json = (JSONObject) DarlingKate.getParser().parse(PostExecutor.executeGetGZIP(url));
                json = (JSONObject) json.get("documents");
                final String urlPrefix = "https://meduza.io/";
                for(String key : (Set<String>) json.keySet()) {
                    JSONObject news = (JSONObject) json.get(key);
                    String title = (String) news.get("title");
                    if(news.containsKey("second_title"))
                        title += " (" + news.get("second_title") + ")";
                    this.news.add(new NewsItem(title, urlPrefix + (String) news.get("url")));
                }
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
    @Data
    private static class NewsItem {
        
        private final String title;
        private final String url;
        
    }

}
