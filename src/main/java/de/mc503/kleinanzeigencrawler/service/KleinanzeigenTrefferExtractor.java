package de.mc503.kleinanzeigencrawler.service;

import de.mc503.kleinanzeigencrawler.model.Gesuch;
import de.mc503.kleinanzeigencrawler.model.Treffer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KleinanzeigenTrefferExtractor {

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<Treffer> extractTreffer(Document doc, Gesuch gesuch) {
        List<Treffer> trefferListe = new ArrayList<>();
        Elements articles = doc.getElementsByTag("article");
        for (Element article : articles) {
            Treffer treffer = new Treffer();
            Elements titleElement = article.getElementsByClass("text-module-begin");
            treffer.setName(titleElement.text());
            String einstellDatumText = article.getElementsByClass("aditem-main--top--right").text();
            String[] split = einstellDatumText.split(", ");
            if (einstellDatumText.startsWith("Heute")) {
                treffer.setHeuteEingestellt(true);
                treffer.setEinstellungsZeit(LocalTime.parse(split[1].trim()));
                treffer.setEinstellungsDatum(LocalDate.now());
            } else if (einstellDatumText.startsWith("Gestern")) {
                treffer.setHeuteEingestellt(false);
                treffer.setEinstellungsZeit(LocalTime.parse(split[1].trim()));
                treffer.setEinstellungsDatum(LocalDate.now().minusDays(1));
            } else {
                treffer.setHeuteEingestellt(false);
                treffer.setEinstellungsZeit(null);
                treffer.setEinstellungsDatum(LocalDate.parse(split[0].trim(), DATE_TIME_FORMATTER));
            }
            treffer.setHref("https://www.kleinanzeigen.de" + article.attr("data-href"));
            trefferListe.add(treffer);
            String currentPriceText = article.getElementsByClass("aditem-main--middle--price-shipping--price").text();
            if (currentPriceText.endsWith("VB")) {
                treffer.setPreisIstVb(true);
                treffer.setPreis(Integer.parseInt(currentPriceText.split(" € ")[0]));
            } else if (currentPriceText.startsWith("Zu verschenk")) {
                treffer.setPreisIstVb(false);
                treffer.setPreis(0);
            } else {
                treffer.setPreisIstVb(false);
                try {
                    treffer.setPreis(Integer.parseInt(currentPriceText.substring(0, currentPriceText.length() - 2)));
                } catch (NumberFormatException e) {
                    log.error("{}\n{} :\n{}", e.getMessage(), treffer.getName(), treffer.getHref());
                }
            }
        }
        return filterTreffer(trefferListe, gesuch);
    }

    private List<Treffer> filterTreffer(List<Treffer> trefferList, Gesuch gesuch) {
        return trefferList.stream()
                .filter(treffer -> containsAny(gesuch.getTrefferBegriffe(), treffer.getName()))
                .filter(treffer -> !treffer.getName().toLowerCase().contains("leerkarton")
                        && !treffer.getName().toLowerCase().contains("nur verpackung")
                        && !treffer.getName().toLowerCase().contains("nur karton")
                        && !treffer.getName().toLowerCase().contains("leerer")
                        && !treffer.getName().toLowerCase().contains("leere")).toList();
    }

    private boolean containsAny(List<String> searches, String text) {
        for (String search : searches) {
            if (text.toLowerCase().contains(search.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
