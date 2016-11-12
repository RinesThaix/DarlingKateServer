package ru.luvas.dk.server.user;

import java.util.List;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public interface Sender {

    void sendMessage(String msg);
    
    void sendMessage(String msg, Object... args);
    
    void sendMessage(List<String> msg);
    
    void sendMessage(List<String> msg, Object... args);
    
}