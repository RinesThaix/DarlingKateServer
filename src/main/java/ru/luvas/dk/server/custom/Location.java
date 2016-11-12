package ru.luvas.dk.server.custom;

import lombok.Data;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
@Data
public class Location {

    private final float longitude;
        
    private final float latitude;

    public float distance(Location loc) {
        return (float) Math.hypot(longitude - loc.longitude, latitude - loc.latitude);
    }
    
}
