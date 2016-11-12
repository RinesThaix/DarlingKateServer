package ru.luvas.dk.server;

import org.springframework.boot.SpringApplication;
import ru.luvas.dk.server.spring.SpringController;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Launcher {

    public static void main(String[] args) {
        new DarlingKate();
        SpringApplication.run(SpringController.class, args);
    }
    
}
