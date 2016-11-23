package ru.luvas.dk.server.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Session {
    
    private final static int SESSION_LIFE_TIME_IN_MINUTES = 10;
    private final static Session CONSOLE_SESSION = new Session("127.0.0.1");
    
    private final static LoadingCache<String, Session> SESSIONS = CacheBuilder.newBuilder()
            .initialCapacity(1000).expireAfterAccess(SESSION_LIFE_TIME_IN_MINUTES, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Session>() {
        @Override
        public Session load(String key) {
            return new Session(key);
        }
    });
    
    public static Session getSession(String ip) {
        return SESSIONS.getUnchecked(ip);
    }
    
    public static Session getConsoleSession() {
        return CONSOLE_SESSION;
    }
    
    @Getter
    private final String ip;
    
    private final Map<String, Object> data = new HashMap<>();
    
    public void set(String key, Object value) {
        put(key, value);
    }
    
    public void put(String key, Object value) {
        data.put(key, value);
    }
    
    public String getString(String key) {
        return (String) get(key);
    }
    
    public int getInt(String key) {
        return (int) get(key);
    }
    
    public Object get(String key) {
        return data.get(key);
    }
    
    public void remove(String key) {
        data.remove(key);
    }
    
    public void invalidate() {
        SESSIONS.invalidate(ip);
    }
    
    public boolean has(String key) {
        return data.containsKey(key);
    }
    
    public boolean contains(String key) {
        return has(key);
    }

}
