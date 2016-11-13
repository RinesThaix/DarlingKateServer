package ru.luvas.dk.server.custom;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.json.simple.JSONObject;
import ru.luvas.dk.server.spring.Errors;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class RequestResult {

    private final String speech;
    private final String message;
    private final String photoUrl;
    private final Errors.Error error;
    
    public RequestResult(String speech, String message, String photoUrl) {
        this.speech = speech;
        this.message = message;
        this.photoUrl = photoUrl;
        this.error = null;
    }
    
    public RequestResult(String answer) {
        this(answer, answer, null);
    }
    
    public RequestResult(Errors.Error error) {
        this.speech = this.message = this.photoUrl = null;
        this.error = error;
    }
    
    public String toJson() {
        if(this.error != null)
            return this.error.toJson();
        if(this.speech == null || this.message == null)
            return Errors.CAN_NOT_HANDLE_REQUEST.toJson();
        //Making our json look beauty (as we did with errors json before)
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("speech", this.speech);
        json.put("message", this.message);
        if(this.photoUrl != null)
            json.put("photo", this.photoUrl);
        return JSONObject.toJSONString(json);
    }
    
}
