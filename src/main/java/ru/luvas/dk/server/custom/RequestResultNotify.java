package ru.luvas.dk.server.custom;

import org.json.simple.JSONObject;

/**
 *
 * @author RINES <iam@kostya.sexy>
 */
public class RequestResultNotify extends RequestResult {
    
    public RequestResultNotify(String answer) {
        this(answer, answer);
    }
    
    public RequestResultNotify(String speech, String text) {
        super(RequestResultType.NOTIFY, RequestResultSpeak.makeData(speech, text, null));
    }

}
