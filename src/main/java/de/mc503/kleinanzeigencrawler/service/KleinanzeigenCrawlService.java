package de.mc503.kleinanzeigencrawler.service;

import de.mc503.kleinanzeigencrawler.enums.Zustand;
import de.mc503.kleinanzeigencrawler.model.Gesuch;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class KleinanzeigenCrawlService {

    public Document kleinanzeigenCrawl(Gesuch gesuch) {
        try {
            return Jsoup.connect(buildUrl(gesuch)).get();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String buildUrl(Gesuch gesuch) {
        StringBuilder url = new StringBuilder();
        url.append("https://www.kleinanzeigen.de/");
        if (gesuch.getPreisVon() != null || gesuch.getPreisBis() != null) {
            appendPreisVonBis(gesuch, url);
            url.append(gesuch.getSuchbegriff().replace(' ', '-'));
            url.append("/");
            url.append("k0");
            appendZustaende(gesuch, url);


        }
        return url.toString();
    }

    private static void appendZustaende(Gesuch gesuch, StringBuilder url) {
        if (!gesuch.getZustaende().isEmpty()) {
            url.append("+global.zustand:");
            for (int i = 0; i < gesuch.getZustaende().size(); i++) {
                Zustand zustand = gesuch.getZustaende().get(i);
                url.append(zustand.getStringValue());
                if (i < gesuch.getZustaende().size() - 1) {
                    url.append("%2C");
                }
            }
        }
    }

    private static void appendPreisVonBis(Gesuch gesuch, StringBuilder url) {
        url.append("s-preis:");
        if (gesuch.getPreisVon() != null) {
            url.append(gesuch.getPreisVon()).append(":");
            if (gesuch.getPreisBis() != null) {
                url.append(gesuch.getPreisBis());
            }
        } else {
            if (gesuch.getPreisBis() != null) {
                url.append(":").append(gesuch.getPreisBis());
            }
        }
        if (gesuch.getPreisVon() != null || gesuch.getPreisBis() != null) {
            url.append("/");
        }
    }
}
