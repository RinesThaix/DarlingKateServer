package ru.luvas.dk.server.util;

import java.util.Random;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class UtilAlgo {
    
    private final static Random R = new Random();
    
    public static int r() {
        return R.nextInt();
    }
    
    public static int r(int bound) {
        return R.nextInt(bound);
    }

}
