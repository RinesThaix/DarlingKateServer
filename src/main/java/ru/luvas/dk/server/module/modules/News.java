package ru.luvas.dk.server.module.modules;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.custom.NewsItem;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.custom.RequestResultNews;
import ru.luvas.dk.server.custom.RequestResultNotify;
import ru.luvas.dk.server.module.Module;
import ru.luvas.dk.server.user.Session;
import ru.luvas.dk.server.util.Logger;
import ru.luvas.dk.server.util.PostExecutor;
import ru.luvas.dk.server.util.Scheduler;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class News extends Module {
    
    private final static int NEWS_SIZE = 50;
    private final static int PRELOAD_NEWS_PER_TICK = 3;
    
    private int currentNewsIndex;
    private final List<NewsItem> news = new ArrayList<>();

    public News() {
        super(Lists.newArrayList("новости", "покажи новости", "выведи новости", "последние новости"));
        Scheduler.run(() -> checkForUpdates(), 0l, 20l, TimeUnit.MINUTES);
        Scheduler.run(() -> preloadFullNews(), 3l, 3l, TimeUnit.SECONDS);
    }

    @Override
    public RequestResult handle(Session session, String msg) {
        if(news.isEmpty())
            return new RequestResultNotify("В данный момент я не могу получить список последних новостей.");
//        session.set(More.KEY_ITERABLE_MODULE, this);
//        session.set(KEY_NEWS_ITERATOR, 0);
        return new RequestResultNews(news);
    }

//    @Override
//    public RequestResult next(Session session) {
//        if(checkForUpdates()) {
//            session.set(KEY_NEWS_ITERATOR, -1);
//            return new RequestResultSpeak("Буквально только что я запросила новую информацию о новостях, поэтому начнем сначала. Скажи еще раз дальше.");
//        }
//        int index = session.getInt(KEY_NEWS_ITERATOR);
//        if(index >= news.size())
//            return new RequestResultSpeak("Мы с тобой дошли до конца списка последних новостей. Такие вот дела.");
//        String news = this.news.get(++index).getTitle();
//        session.set(KEY_NEWS_ITERATOR, index);
//        return new RequestResult(news);
//    }
    
    private void checkForUpdates() {
        Logger.log("Started preloading news..");
        this.currentNewsIndex = 0;
        news.clear();
        String url = "https://meduza.io/api/v3/search?chrono=news&page=0&per_page=" + NEWS_SIZE + "&locale=ru";
        try {
            JSONObject json = (JSONObject) DarlingKate.getParser().parse(PostExecutor.executeGetGZIP(url));
            json = (JSONObject) json.get("documents");
            final String urlPrefix = "https://meduza.io/", urlPrefixSubtracted = urlPrefix.substring(0, urlPrefix.length() - 1);
            for(String key : (Set<String>) json.keySet()) {
                JSONObject news = (JSONObject) json.get(key);
                String title = (String) news.get("title");
                if(news.containsKey("second_title")) {
                    String secondTitle = (String) news.get("second_title");
                    if(!secondTitle.isEmpty())
                        title += " (" + secondTitle + ")";
                }
                String imageUrl = null;
                if(news.containsKey("image")) {
                    JSONObject img = (JSONObject) news.get("image");
                    if(img.containsKey("small_url"))
                        imageUrl = urlPrefixSubtracted + img.get("small_url");
                }
                this.news.add(new NewsItem(key, title, null, imageUrl));
            }
            Logger.log("First stage of news preloading is done.");
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void preloadFullNews() {
        if(this.currentNewsIndex >= this.news.size())
            return;
        int max = this.currentNewsIndex + PRELOAD_NEWS_PER_TICK;
        final String urlPrefix = "https://meduza.io/api/v3/";
        for(; this.currentNewsIndex < Math.min(this.news.size(), max); ++this.currentNewsIndex) {
            NewsItem ni = this.news.get(this.currentNewsIndex);
            try {
                JSONObject json = (JSONObject) DarlingKate.getParser().parse(PostExecutor.executeGetGZIP(urlPrefix + ni.getExactUrl()));
                json = (JSONObject) json.get("root");
                ni.setFullText((String) json.get("description"));
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        if(this.currentNewsIndex == this.news.size())
            Logger.log("All news have been preloaded!");
    }

}
