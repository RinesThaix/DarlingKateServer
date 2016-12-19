package ru.luvas.dk.server.custom;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author RINES <iam@kostya.sexy>
 */
public class RequestResultNews extends RequestResult {

    public RequestResultNews(List<NewsItem> news) {
        super(RequestResultType.NEWS, makeData(news));
    }
    
    private static JSONObject makeData(List<NewsItem> news) {
        JSONObject json = new JSONObject();
        json.put("total_news", news.size());
        JSONArray array = new JSONArray();
        news.forEach(n -> {
            JSONObject jn = new JSONObject();
            jn.put("title", n.getTitle());
            jn.put("full_text", n.getFullText());
            if(n.getPhotoUrl() != null)
                jn.put("photo_url", n.getPhotoUrl());
            jn.put("news_url", "https://meduza.io/" + n.getExactUrl());
            array.add(jn);
        });
        json.put("news", array);
        return json;
    }

}
