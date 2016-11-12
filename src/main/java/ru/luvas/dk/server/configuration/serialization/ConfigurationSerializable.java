package ru.luvas.dk.server.configuration.serialization;

import java.util.Map;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public interface ConfigurationSerializable {

    public Map<String, Object> serialize();
    
}
