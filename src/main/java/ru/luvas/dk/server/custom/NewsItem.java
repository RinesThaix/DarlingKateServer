package ru.luvas.dk.server.custom;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author RINES <iam@kostya.sexy>
 */
@Data
@AllArgsConstructor
public class NewsItem {

    private String exactUrl, title, fullText, photoUrl;
    
}
