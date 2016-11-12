package ru.luvas.dk.server.configuration;

import java.util.Map;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public interface Configuration extends ConfigurationSection {

    public void addDefaults(Map<String, Object> defaults);
    
    public void addDefaults(Configuration defaults);
    
    public void setDefaults(Configuration defaults);
    
    public Configuration getDefaults();
    
    public ConfigurationOptions options();
    
}
