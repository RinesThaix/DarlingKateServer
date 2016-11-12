package ru.luvas.dk.server.user;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.luvas.dk.server.util.Logger;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Console implements Sender {
    
    @Getter
    private final static Console instance = new Console();

    @Override
    public void sendMessage(String msg) {
        Logger.log(msg);
    }

    @Override
    public void sendMessage(String msg, Object... args) {
        sendMessage(String.format(msg, args));
    }

    @Override
    public void sendMessage(List<String> msg) {
        StringBuilder sb = new StringBuilder();
        msg.forEach(m -> sb.append(m).append("\n"));
        sendMessage(sb.toString().trim());
    }

    @Override
    public void sendMessage(List<String> msg, Object... args) {
        StringBuilder sb = new StringBuilder();
        msg.forEach(m -> sb.append(m).append("\n"));
        sendMessage(String.format(sb.toString().trim(), args));
    }

}
