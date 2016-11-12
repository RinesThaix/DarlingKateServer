package ru.luvas.dk.server.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
@RequiredArgsConstructor
public class ConfigurationOptions {
    
    private char pathSeparator = '.';
    private boolean copyDefaults = false;
    private final Configuration configuration;

}
