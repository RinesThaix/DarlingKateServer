package ru.luvas.dk.server.spring;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Protector {
    
    private final static long USE_LASTS_FOR_IN_SECONDS = 60;
    private final static long MAX_USES = 20;
    private final static long BAN_TIME_IN_MINUTES = 3;
    
    /**
     * Key: IP; Value: Priority queue of uses times.
     */
    private final static LoadingCache<String, PriorityQueue<Long>> usesCache = CacheBuilder.newBuilder()
            .initialCapacity(1000).expireAfterAccess(USE_LASTS_FOR_IN_SECONDS << 1, TimeUnit.SECONDS)
            .build(new CacheLoader<String, PriorityQueue<Long>>() {
        @Override
        public PriorityQueue<Long> load(String key) {
            return new PriorityQueue<>((a, b) -> a < b ? -1 : 1);
        }
    });
    
    /**
     * Key: IP; Value: Banned until.
     */
    private final static LoadingCache<String, Long> bannedCache = CacheBuilder.newBuilder()
            .initialCapacity(10).expireAfterWrite(BAN_TIME_IN_MINUTES, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Long>() {
        @Override
        public Long load(String key) {
            return System.currentTimeMillis() + BAN_TIME_IN_MINUTES * 60000l;
        }
    });

    /**
     * Checks whether ip is spam-banned.
     * @param ip the address of the target.
     * @return true whether given ip is banned, false otherwise.
     */
    static boolean checkIfSpamBanned(String ip) {
        long current = System.currentTimeMillis();
        Long bannedUntil = bannedCache.getIfPresent(ip);
        if(bannedUntil != null) {
            if(current < bannedUntil)
                return true;
            bannedCache.invalidate(ip);
        }
        PriorityQueue<Long> uses = usesCache.getUnchecked(ip);
        long lastCorner = current - USE_LASTS_FOR_IN_SECONDS * 1000l;
        while(!uses.isEmpty() && uses.peek() < lastCorner)
            uses.poll();
        uses.add(current);
        if(uses.size() > MAX_USES) {
            bannedCache.getUnchecked(ip);
            return true;
        }
        return false;
    }
    
}
