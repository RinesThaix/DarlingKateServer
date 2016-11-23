package ru.luvas.dk.server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import jline.console.ConsoleReader;
import lombok.Getter;
import org.fusesource.jansi.AnsiConsole;
import org.json.simple.parser.JSONParser;
import ru.luvas.dk.server.command.CommandManager;
import ru.luvas.dk.server.configuration.ConfigurationManager;
import ru.luvas.dk.server.configuration.FileConfiguration;
import ru.luvas.dk.server.event.EventManager;
import ru.luvas.dk.server.event.events.ChatEvent;
import ru.luvas.dk.server.event.listener.SystemListener;
import ru.luvas.dk.server.logger.LoggingOutputStream;
import ru.luvas.dk.server.logger.OwnLogger;
import ru.luvas.dk.server.module.ModuleManager;
import ru.luvas.dk.server.neural.Classifier;
import ru.luvas.dk.server.spring.Authenticator;
import ru.luvas.dk.server.util.Logger;
import ru.luvas.dk.server.util.Scheduler;
import ru.luvas.dk.server.util.sql.Connector;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class DarlingKate {
    
    @Getter
    private static DarlingKate instance;
    
    @Getter
    private static Connector connector;
    
    @Getter
    private static int wekaCapacity;
    
    @Getter
    private static Classifier classifier;
    
    @Getter
    private final static JSONParser parser = new JSONParser();
    
    public DarlingKate() {
        if(instance != null)
            throw new IllegalStateException("Another instance of DarlingKateServer is already there!");
        instance = this;
        preloadLogger();
        Logger.section("configuration");
        FileConfiguration config = getConfig("main");
        if(!config.isSet("secret_phrase")) {
            config.set("weka_capacity", 100_000);
            config.set("secret_phrase", "0");
            saveConfig("main");
        }
        wekaCapacity = config.getInt("weka_capacity");
        String secretPhrase = config.getString("secret_phrase");
        if(!secretPhrase.equals("0"))
            Authenticator.setupSecretHash(secretPhrase);
        //Don't think we need the following at the moment
//        config = getConfig("database");
//        if(!config.isSet("host")) {
//            config.set("host", "localhost");
//            config.set("port", 3306);
//            config.set("username", "root");
//            config.set("password", "root");
//            config.set("dbname", "darlingkate");
//            saveConfig("database");
//        }
//        connector = new ConnectorBuilder("SQL",
//                config.getString("host") + ":" + config.getInt("port"),
//                config.getString("username"),
//                config.getString("password"),
//                config.getString("dbname")
//        ).build(true);
        Logger.section("commands");
        CommandManager.init();
        Logger.section("modules");
        ModuleManager.init();
        Logger.section("event handlers");
        registerHandlers();
        Logger.section("weka classifier");
        classifier = new Classifier();
        Logger.log("Preloading SMO..");
        classifier.classify("привет"); //to preload SMO
        Logger.log("DarlingKateServer has been initialized, starting console thread & launching spring boot..");
    }
    
    public void disable() {
        Logger.log("Disabling DarlingKateServer..");
        classifier.save();
        Connector.shutdownAll();
        Scheduler.getExecutor().shutdown();
        Scheduler.sleep(2000l);
        System.exit(0);
    }
    
    public void reloadClassifier() {
        classifier.save();
        classifier.invalidate();
        classifier = new Classifier();
    }
    
    private String getImplVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        if(version == null)
            return "unknown";
        return version;
    }
    
    public String getCommitId() {
        String version = getImplVersion();
        if(version.equals("unknown"))
            return version;
        return version.split("\\-")[0];
    }
    
    public String getCommitDate() {
        String version = getImplVersion();
        if(version.equals("unknown"))
            return "";
        return version.split("\\-")[1];
    }
    
    private void preloadLogger() {
        //This is probably the weirdest bug I've ever expected in my life:
        //just trust me that this line is necessary.
        System.setProperty("library.jansi.version", "DarlingKate");
        AnsiConsole.systemInstall();
        ConsoleReader consoleReader;
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setExpandEvents(false);
            OwnLogger logger = new OwnLogger(consoleReader);
            System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
            Logger.setLogger(logger);
        }catch(IOException ex) {
            throw new IllegalStateException("Could not load console worker!");
        }
        Logger.log("Enabling DarlingKateServer..");
        Scheduler.run(() -> {
            while(true) {
                try {
                    String line = consoleReader.readLine("> ");
                    new ChatEvent(line).call();
                }catch(Exception ex) {
                    Logger.warn("Can not handle command from console!");
                }
            }
        }, 1, TimeUnit.SECONDS);
    }
    
    private void registerHandlers() {
        EventManager.register(new SystemListener());
        Logger.log("SystemListener (aka system event handler) has successfully been registered!");
    }
    
    public FileConfiguration getConfig(String name) {
        return ConfigurationManager.getConfig(name);
    }
    
    public void saveConfig(String name) {
        ConfigurationManager.saveConfig(name);
    }
    
    public void reloadConfig(String name) {
        ConfigurationManager.reloadConfig(name);
    }

}
