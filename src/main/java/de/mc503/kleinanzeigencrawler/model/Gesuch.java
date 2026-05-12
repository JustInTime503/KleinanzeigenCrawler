package de.mc503.kleinanzeigencrawler.model;

import de.mc503.kleinanzeigencrawler.enums.Zustand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gesuch {
    private String suchbegriff;
    private List<Zustand> zustaende = new ArrayList<Zustand>();
    private Integer preisVon;
    private Integer preisBis;
    private List<String> trefferBegriffe;

    private List<String> foundHrefs;
}
