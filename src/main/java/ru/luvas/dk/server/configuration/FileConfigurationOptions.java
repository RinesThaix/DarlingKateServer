package ru.luvas.dk.server.configuration;

import lombok.Data;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class FileConfigurationOptions extends ConfigurationOptions {
    
    private String header;
    private boolean copyHeader = true;

    public FileConfigurationOptions(Configuration configuration) {
        super(configuration);
    }

}
