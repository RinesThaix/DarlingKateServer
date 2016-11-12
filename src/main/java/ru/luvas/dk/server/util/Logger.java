package ru.luvas.dk.server.util;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Logger {
    
    private static java.util.logging.Logger logger = null;
    
    public static void setLogger(java.util.logging.Logger logger) {
        Logger.logger = logger;
    }
    
    public static void section(String sectionName) {
        log("-===Entering %s section===-", sectionName);
    }

    public static void log(String s) {
        logger.info(s);
    }
    
    public static void log(String s, Object... args) {
        log(String.format(s, args));
    }
    
    public static void warn(String s) {
        logger.warning(s);
    }
    
    public static void warn(String s, Object... args) {
        warn(String.format(s, args));
    }
    
    public static void warn(Exception ex, String s) {
        logger.warning(s);
        ex.printStackTrace();
    }
    
    public static void warn(Exception ex, String s, Object... args) {
        warn(ex, String.format(s, args));
    }
    
}
