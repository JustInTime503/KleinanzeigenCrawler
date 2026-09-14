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
            String einstellDatumText = null;
            Element calendar = article.select("svg[data-title=calendarOutline]").first();
            if (calendar != null) {
                Element dateElement = calendar.parent().select("span").first();
                if (dateElement != null) {
                    einstellDatumText = dateElement.text();
                } else {
                    continue;
                }
            } else {
                continue;
            }
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

            boolean versandMoeglich = article.select("span[data-dhl-promotion]")
                    .stream()
                    .anyMatch(e -> e.text().equals("Versand möglich"));
            treffer.setIsVersand(versandMoeglich); // direkt kaufen geht nicht ohne versand

            Element titelElement = article.select("h3").first();
            if (titelElement != null) {
                String titelString = titelElement.text();
                treffer.setName(titelString);
            }
            if (treffer.getName() == null) {
                log.error("Could not find name for article {}", article.attr("data-href"));
                continue;
            }
            treffer.setHref("https://www.kleinanzeigen.de" + article.attr("data-href"));
            trefferListe.add(treffer);
            Element preisElement = article.select("p.text-title3.font-strong").first();
            if (preisElement != null) {
                String preis = preisElement.text();
                if (preis.endsWith("VB")) {
                    treffer.setPreisIstVb(true);
                    treffer.setPreis(Integer.parseInt(preis.split(" € ")[0]));
                } else if (preis.startsWith("Zu verschenk")) {
                    treffer.setPreisIstVb(false);
                    treffer.setPreis(0);
                } else {
                    treffer.setPreisIstVb(false);
                    try {
                        treffer.setPreis(Integer.parseInt(preis.substring(0, preis.length() - 2)));
                    } catch (NumberFormatException e) {
                        log.error("{}\n{} :\n{}", e.getMessage(), treffer.getName(), treffer.getHref());
                    }
                }
            }
        }
        return filterTreffer(trefferListe, gesuch);
    }

    private List<Treffer> filterTreffer(List<Treffer> trefferList, Gesuch gesuch) {
        return trefferList.stream()
                .filter(treffer -> containsAny(gesuch.getTrefferBegriffe(), treffer.getName()))
                .filter(treffer -> !containsAny(gesuch.getBlacklist(), treffer.getName()))
                .filter(treffer -> {
                    if (gesuch.getNurVersand()) {
                        return treffer.getIsVersand();
                    } else return true;
                })
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
