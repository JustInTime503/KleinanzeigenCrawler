package de.mc503.kleinanzeigencrawler.service;

import de.mc503.kleinanzeigencrawler.model.Gesuch;
import de.mc503.kleinanzeigencrawler.model.Treffer;
import de.mc503.kleinanzeigencrawler.service.mail.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KleinanzeigenService {

    private final KleinanzeigenCrawlService kleinanzeigenCrawlService;

    private final KleinanzeigenTrefferExtractor kleinanzeigenTrefferExtractor;

    private final MailSenderService mailSenderService;


    private final List<Treffer> treffersSentLinksFor = new ArrayList<>();

    public void fetchListingsAndMessageOnNew(Gesuch gesuch) {
        List<Treffer> treffers = retrieveCurrentListings(gesuch);
        log.info("Found {} listings", treffers.size());
        for (Treffer treffer : treffers) {
            if (!treffersSentLinksFor.contains(treffer)) {
                mailSenderService.sendMail(treffer.getPreis() + ": " + treffer.getName(), treffer.getHref(), gesuch.getMail());
                treffersSentLinksFor.add(treffer);
            }
        }
    }

    private List<Treffer> retrieveCurrentListings(Gesuch gesuch) {
        log.info("Crawling for KleinanzeigenJob gesuch: {}", gesuch.getSuchbegriff());
        Document page = kleinanzeigenCrawlService.kleinanzeigenCrawl(gesuch);
        return kleinanzeigenTrefferExtractor.extractTreffer(page, gesuch);
    }
}
