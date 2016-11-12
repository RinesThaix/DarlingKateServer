package ru.luvas.dk.server.configuration.yaml;

import lombok.Data;
import ru.luvas.dk.server.configuration.Configuration;
import ru.luvas.dk.server.configuration.FileConfigurationOptions;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class YamlConfigurationOptions extends FileConfigurationOptions {
    
    private int indent = 2;

    public YamlConfigurationOptions(Configuration configuration) {
        super(configuration);
    }

}
