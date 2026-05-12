package de.mc503.kleinanzeigencrawler.service;

import de.mc503.kleinanzeigencrawler.enums.Zustand;
import de.mc503.kleinanzeigencrawler.model.Gesuch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KleinanzeigenCrawlServiceTest {

    KleinanzeigenCrawlService crawlService = new KleinanzeigenCrawlService();

    @Test
    public void testBuildUrl() {
        Gesuch gesuch = new Gesuch();
        gesuch.setPreisVon(10);
        gesuch.setPreisBis(1000);
        gesuch.setSuchbegriff("rtx 4070");
        gesuch.setTrefferBegriffe(new ArrayList<>(List.of("geforce rtx 4070", "geforce rtx 4070 ti")));
        gesuch.setZustaende(new ArrayList<>(List.of(Zustand.NEU, Zustand.GUT)));

        assertEquals("https://www.kleinanzeigen.de/s-preis:10:1000/rtx-4070/k0+global.zustand:new%2Cok", crawlService.buildUrl(gesuch));
    }
}
