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

    //@Autowired
    //private KleinanzeigenJob kleinanzeigenJob;
//
    //@Test
    //public void helperTest() {
    //    KleinanzeigenCrawlService crawlService = new KleinanzeigenCrawlService();
//
    //    Gesuch gesuch = new Gesuch();
    //    gesuch.setPreisBis(700);
    //    gesuch.setSuchbegriff("rtx 4070");
    //    gesuch.setTrefferBegriffe(new ArrayList<>(List.of("geforce rtx 4070", "geforce rtx 4070 ti")));
    //    gesuch.setZustaende(new ArrayList<>(List.of(Zustand.NEU, Zustand.GUT)));
    //    Document result = crawlService.kleinanzeigenCrawl(gesuch);
    //    new KleinanzeigenTrefferExtractor().extractTreffer(result, gesuch);
    //}
//
    //@Test
    //public void kleinanzeigenTrefferTest() {
    //    kleinanzeigenJob.run();
    //}
}
