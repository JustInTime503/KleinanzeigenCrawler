package de.mc503.kleinanzeigencrawler;

import de.mc503.kleinanzeigencrawler.enums.Zustand;
import de.mc503.kleinanzeigencrawler.jobs.KleinanzeigenJob;
import de.mc503.kleinanzeigencrawler.model.Gesuch;
import de.mc503.kleinanzeigencrawler.service.GesuchReaderService;
import de.mc503.kleinanzeigencrawler.service.KleinanzeigenCrawlService;
import de.mc503.kleinanzeigencrawler.service.KleinanzeigenTrefferExtractor;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HelperTests {

    @Autowired
    private KleinanzeigenJob kleinanzeigenJob;

    @Test
    public void helperTest() {
//        KleinanzeigenCrawlService crawlService = new KleinanzeigenCrawlService();

//        Gesuch gesuch = new Gesuch();
//        gesuch.setSuchbegriff("5600x");
//        gesuch.setPreisVon(50);
//        gesuch.setPreisBis(1000);
//        gesuch.setTrefferBegriffe(new ArrayList<>(List.of("5600x")));
//        gesuch.setZustaende(new ArrayList<>(List.of(Zustand.NEU, Zustand.GUT, Zustand.IN_ORDNUNG, Zustand.SEHR_GUT)));
//        gesuch.setBlacklist(new ArrayList<>(List.of("leerkarton", "leer", "nur verpackung", "leerverpackung")));
//        gesuch.setMail("mc503.kleincrawl@gmail.com");
//        gesuch.setNurVersand(true);
//        Document result = crawlService.kleinanzeigenCrawl(gesuch);
//        new KleinanzeigenTrefferExtractor().extractTreffer(result, gesuch);
    }

    @Test
    public void kleinanzeigenTrefferTest() {
        kleinanzeigenJob.run();
    }
}
