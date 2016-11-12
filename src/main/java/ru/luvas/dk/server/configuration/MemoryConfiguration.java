package ru.luvas.dk.server.configuration;

import java.util.Map;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.Validate;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@NoArgsConstructor
public class MemoryConfiguration extends MemorySection implements Configuration {
    
    protected Configuration defaults;
    protected ConfigurationOptions options;
    
    public MemoryConfiguration(Configuration defaults) {
        this.defaults = defaults;
    }
    
    @Override
    public void addDefault(String path, Object value) {
        Validate.notNull(path, "Path can't be null");
        if(defaults == null)
            defaults = new MemoryConfiguration();
        defaults.set(path, value);
    }
    
    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    @Override
    public void addDefaults(Map<String, Object> defaults) {
        Validate.notNull(defaults, "Defaults can't be null");
        defaults.entrySet().forEach(e -> addDefault(e.getKey(), e.getValue()));
    }

    @Override
    public void addDefaults(Configuration defaults) {
        Validate.notNull(defaults, "Defaults can't be null");
        addDefaults(defaults.getValues(true));
    }

    @Override
    public void setDefaults(Configuration defaults) {
        Validate.notNull(defaults, "Defaults can't be null");
        this.defaults = defaults;
    }

    @Override
    public Configuration getDefaults() {
        return this.defaults;
    }

    @Override
    public ConfigurationOptions options() {
        if(options == null)
            options = new ConfigurationOptions(this);
        return options;
    }

}
