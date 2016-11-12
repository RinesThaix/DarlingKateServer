package ru.luvas.dk.server.spring;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import org.json.simple.JSONObject;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Errors {

    private static int id = 0;
    private final static Map<Integer, Error> errors = new HashMap<>();
    
    public final static Error
            UNEXPECTED_ERROR = new Error(id++, "Unexpected error occured whilst trying to handle your request."),
            TOO_MANY_REQUESTS = new Error(id++, "You made too many requests within given amount of time. Try again later."),
            NO_MESSAGE = new Error(id++, "There's no message in your request."),
            TOO_LONG_MESSAGE = new Error(id++, "This message is too long."),
            WRONG_LOCATION_FORMAT = new Error(id++, "Wrong location format."),
            REQUEST_WAS_DENIED = new Error(id++, "By some reason this request was denied."),
            CAN_NOT_HANDLE_REQUEST = new Error(id++, "By some reason we are unable to handle your request.");
    
    public static Error get(int id) {
        return errors.get(id);
    }
    
    public static class Error {
        
        @Getter
        private final int id;
        
        @Getter
        private final String message;
        
        private final String json;
        
        private Error(int id, String message) {
            this.id = id;
            this.message = message;
            //Using LinkedHashMap just to make json string ordered (it looks nicier)
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("error", true);
            map.put("id", id);
            map.put("text", message);
            this.json = JSONObject.toJSONString(map);
            errors.put(id, this);
        }
        
        public String toJson() {
            return json;
        }
        
    }
    
}
