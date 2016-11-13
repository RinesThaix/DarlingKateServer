package ru.luvas.dk.server.spring;

import javax.servlet.http.HttpServletRequest;
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
            @RequestParam(name="location", required=false) String slocation, 
            @RequestParam(name="token", required=false) String token, HttpServletRequest request) {
        try {
            String ip = request.getHeader("HTTP_CF_CONNECTING_IP");
            if(ip == null)
                ip = request.getRemoteAddr();
            if(Protector.checkIfSpamBanned(ip))
                return Errors.TOO_MANY_REQUESTS.toJson();
            if(token == null)
                return Errors.NO_TOKEN.toJson();
            Errors.Error error = Authenticator.validateToken(token);
            if(error != null)
                return error.toJson();
            if(message == null)
                return Errors.NO_MESSAGE.toJson();
            if(message.length() > 64)
                return Errors.TOO_LONG_MESSAGE.toJson();
            Location location = null;
            if(slocation != null) {
                String[] spl = slocation.split(";");
                try {
                    location = new Location(Float.parseFloat(spl[0]), Float.parseFloat(spl[1]));
                }catch(Exception ex) {
                    return Errors.WRONG_LOCATION_FORMAT.toJson();
                }
            }
            RequestEvent reqEvent = new RequestEvent(message, location);
            reqEvent.call();
            if(reqEvent.isCancelled())
                return Errors.REQUEST_WAS_DENIED.toJson();
            RequestResult result = reqEvent.getResult();
            if(result == null)
                return Errors.CAN_NOT_HANDLE_REQUEST.toJson();
            return result.toJson();
        }catch(Exception ex) {
            return Errors.UNEXPECTED_ERROR.toJson();
        }
    }
    
}