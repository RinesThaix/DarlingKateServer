package ru.luvas.dk.server.spring;

import org.json.simple.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.luvas.dk.server.custom.Location;
import ru.luvas.dk.server.custom.RequestResult;
import ru.luvas.dk.server.event.events.RequestEvent;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Controller
@EnableAutoConfiguration
public class SpringController {
    
    @RequestMapping("/")
    @ResponseBody
    public String getAnswer(@RequestParam(name="message", required=false) String message,
            @RequestParam(name="location", required=false) String slocation) {
        try {
            if(message == null)
                return error("There's no message in your request");
            if(message.length() > 64)
                return error("This message is too long");
            Location location = null;
            if(slocation != null) {
                String[] spl = slocation.split(";");
                try {
                    location = new Location(Float.parseFloat(spl[0]), Float.parseFloat(spl[1]));
                }catch(Exception ex) {
                    return error("Wrong location format");
                }
            }
            RequestEvent reqEvent = new RequestEvent(message, location);
            reqEvent.call();
            if(reqEvent.isCancelled())
                return error("By some reason this request was denied");
            RequestResult result = reqEvent.getResult();
            if(result == null)
                return error("By some reason we are unable to handle your request");
            return result.toJson();
        }catch(Exception ex) {
            return error();
        }
    }
    
    private String error() {
        return error("Unexpected error occured whilst trying to handle your request");
    }
    
    private String error(String message) {
        JSONObject json = new JSONObject();
        json.put("error", true);
        json.put("text", message);
        return json.toJSONString();
    }
    
}