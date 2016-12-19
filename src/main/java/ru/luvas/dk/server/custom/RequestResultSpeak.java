package ru.luvas.dk.server.custom;

import org.json.simple.JSONObject;

/**
 *
 * @author RINES <iam@kostya.sexy>
 */
public class RequestResultSpeak extends RequestResult {
    
    public RequestResultSpeak(String answer) {
        this(answer, answer);
    }
    
    public RequestResultSpeak(String speech, String text) {
        this(speech, text, null);
    }

    public RequestResultSpeak(String speech, String text, String photo) {
        super(RequestResultType.SPEAK, makeData(speech, text, photo));
    }
    
    final static JSONObject makeData(String speech, String text, String photo) {
        JSONObject json = new JSONObject();
        json.put("speech", speech);
        json.put("text", text);
        if(photo != null)
            json.put("photo", photo);
        return json;
    }

}
