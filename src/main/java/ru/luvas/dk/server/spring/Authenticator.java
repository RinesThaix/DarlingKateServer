package ru.luvas.dk.server.spring;

import java.math.BigInteger;
import ru.luvas.dk.server.util.Rand;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Authenticator {
    
    private final static long TOKEN_EXPIRATION_TIME_IN_MILLISECONDS = 30000l;
    private final static int MAX_POWER = 5;
    
    private static Integer secretHash = null;
    
    public static void setupSecretHash(String secretPhrase) {
        int hash = Math.abs(secretPhrase.hashCode());
        StringBuilder sb = new StringBuilder(hash);
        while(sb.length() > MAX_POWER)
            sb.deleteCharAt(0);
        while(sb.length() < MAX_POWER)
            sb.append(Rand.nextInt(10));
        secretHash = Integer.parseInt(sb.toString());
    }

    public static Errors.Error validateToken(String token) {
        if(secretHash == null)
            return null;
        try {
            long current = System.currentTimeMillis();
            BigInteger bi = new BigInteger(token, 25);
            char[] chars = bi.toString().toCharArray();
            StringBuilder global = new StringBuilder();
            for(int i = 0; i < chars.length; i += MAX_POWER) {
                StringBuilder sb = new StringBuilder();
                for(int j = i; j < i + MAX_POWER; ++j)
                    sb.append(chars[j]);
                int value = Integer.parseInt(sb.toString());
                value ^= secretHash;
                String svalue = String.valueOf(value);
                while(svalue.length() > 1 && svalue.charAt('0') == 0)
                    svalue = svalue.substring(1);
                global.append(svalue);
            }
            long result = Long.parseLong(global.toString());
            return current - result > -TOKEN_EXPIRATION_TIME_IN_MILLISECONDS &&
                    current - result < TOKEN_EXPIRATION_TIME_IN_MILLISECONDS ? null : Errors.EXPIRED_TOKEN;
        }catch(Exception ex) {
            return Errors.INVALID_TOKEN;
        }
    }
    
    public static String generateToken() {
        if(secretHash == null)
            return null;
        long current = System.currentTimeMillis();
        char[] chars = String.valueOf(current).toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chars.length; ++i) {
            int value = ((chars[i] - '0') ^ secretHash);
            String svalue = String.valueOf(value);
            while(svalue.length() < MAX_POWER)
                svalue = '0' + svalue;
            sb.append(svalue);
        }
        return new BigInteger(sb.toString()).toString(25);
    }
    
}
