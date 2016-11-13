package ru.luvas.dk.server.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Rand {
    
    private final static Random R = new Random();
    
    @SafeVarargs
    public static <T> T of(T... args) {
        return args[nextInt(args.length)];
    }
    
    public static <T> T of(Collection<T> collection) {
        if(collection instanceof List) {
            return of((List<T>) collection);
        }else {
            int index = nextInt(collection.size());
            Iterator<T> it = collection.iterator();
            for(int i = 0; i < index; i++)
                it.next();
            return it.next();
        }
    }
    
    public static <T> T of(List<T> list) {
        return list.get(nextInt(list.size()));
    }
    
    @SafeVarargs
    public static <T> T of(List<T>... lists) {
        int var = 0;
        for(List<T> l : lists)
            var += l.size();
        var = nextInt(var);
        for(List<T> l : lists)
            if(var >= l.size())
                var -= l.size();
            else
                return l.get(var);
        throw new IllegalArgumentException("All of received lists are empty!");
    }
    
    public static <T extends Enum> T of(Class<T> enumClazz) {
        return Rand.of(enumClazz.getEnumConstants());
    }
    
    public static int intRange(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return min + nextInt(max - min + 1);
    }
    
    public static float floatRange(float from, float to) {
        float min = Math.min(from, to);
        float max = Math.max(from, to);
        return nextFloat() * (max - min) + min;
    }
    
    public static double doubleRange(double from, double to) {
        double min = Math.min(from, to);
        double max = Math.max(from, to);
        return nextDouble() * (max - min) + min;
    }
    
    public static float nextFloat() {
        return R.nextFloat();
    }
    
    public static double nextDouble() {
        return R.nextDouble();
    }
    
    public static boolean nextBoolean() {
        return R.nextBoolean();
    }
    
    public static int nextInt(int bound) {
        return R.nextInt(bound);
    }
    
    public static int nextInt(long seed) {
        return new Random(seed).nextInt();
    }
    
    public static int nextInt(long seed, int bound) {
        return new Random(seed).nextInt(bound);
    }
    
    public static Random getRandom() {
        return R;
    }

}
