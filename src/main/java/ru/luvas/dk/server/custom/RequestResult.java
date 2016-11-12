package ru.luvas.dk.server.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.simple.JSONObject;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
@AllArgsConstructor
public class RequestResult {

    private final String speech;
    private final String message;
    private final String photoUrl;
    
    public RequestResult(String answer) {
        this.speech = this.message = answer;
        this.photoUrl = null;
    }
    
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("speech", this.speech);
        json.put("message", this.message);
        if(this.photoUrl != null)
            json.put("photo", this.photoUrl);
        return json.toJSONString();
    }
    
}
