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

    private final static Map<Integer, Error> ERRORS = new HashMap<>();
    
    public final static Error
            UNEXPECTED_ERROR = new Error(0, "Unexpected error occured whilst trying to handle your request."),
            TOO_MANY_REQUESTS = new Error(1, "You made too many requests within a given period of time. Try again later."),
            NO_MESSAGE = new Error(2, "There's no message in your request."),
            TOO_LONG_MESSAGE = new Error(3, "This message is too long."),
            WRONG_LOCATION_FORMAT = new Error(4, "Wrong location format."),
            REQUEST_WAS_DENIED = new Error(5, "By some reason your request was denied."),
            CAN_NOT_HANDLE_REQUEST = new Error(6, "By some reason we are unable to handle your request."),
            MODULE_TEMPORARILY_DISABLED = new Error(7, "This command is temporarily disabled."),
            NO_TOKEN = new Error(8, "There's no token in your request."),
            INVALID_TOKEN = new Error(9, "Your token is invalid."),
            EXPIRED_TOKEN = new Error(10, "Your token has expired.");
    
    public static Error get(int id) {
        return ERRORS.get(id);
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
            ERRORS.put(id, this);
        }
        
        public String toJson() {
            return json;
        }
        
    }
    
}
