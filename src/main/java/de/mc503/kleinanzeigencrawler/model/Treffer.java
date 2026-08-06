package de.mc503.kleinanzeigencrawler.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Treffer {
    private String name;
    private int preis;
    private boolean preisIstVb;
    private boolean heuteEingestellt;
    private LocalTime einstellungsZeit;
    private LocalDate einstellungsDatum;
    private String href;
    private Boolean isVersand;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Treffer) {
            if (this == o) return true;
            return this.href.equals(((Treffer) o).href);
        }
        return false;
    }
}
