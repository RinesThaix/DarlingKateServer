package ru.luvas.dk.server.configuration.yaml;

import java.io.File;
import java.io.IOException;
import lombok.Getter;
import ru.luvas.dk.server.configuration.FileConfiguration;
import ru.luvas.dk.server.util.Logger;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class LuvasConfiguration {
    
    @Getter
    private final String name;
    private final File file;
    private FileConfiguration config;

    public LuvasConfiguration(String name) {
        this.name = name;
        this.file = new File(name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        if(config == null)
            this.reload();
        return config;
    }

    public void save() {
        if(file == null || config == null)
            return;
        try {
            get().save(file);
        }catch (IOException ex) {
            Logger.warn(ex, "Can't save configuration %s!", getName());
        }
    }
    
}
