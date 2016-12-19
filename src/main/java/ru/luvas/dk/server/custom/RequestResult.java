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

    private final RequestResultType type;
    private final JSONObject data;
    private final Errors.Error error;
    
    public RequestResult(RequestResultType type, JSONObject data) {
        this.type = type;
        this.data = data;
        this.error = null;
    }
    
    public RequestResult(Errors.Error error) {
        this.type = RequestResultType.ERROR;
        this.data = null;
        this.error = error;
    }
    
    public final String toJson() {
        JSONObject data;
        if(this.data != null)
            data = this.data;
        else if(this.error != null)
            return this.error.getJson().toJSONString();
        else
            return Errors.CAN_NOT_HANDLE_REQUEST.getJson().toJSONString();
        data.put("result_type", this.type.name());
        return data.toJSONString();
    }
    
}
