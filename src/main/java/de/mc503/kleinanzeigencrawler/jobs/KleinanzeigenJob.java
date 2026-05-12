package de.mc503.kleinanzeigencrawler.jobs;

import de.mc503.kleinanzeigencrawler.model.Gesuch;
import de.mc503.kleinanzeigencrawler.service.GesuchReaderService;
import de.mc503.kleinanzeigencrawler.service.KleinanzeigenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class KleinanzeigenJob {

    private final GesuchReaderService gesuchReaderService;

    private final KleinanzeigenService kleinanzeigenService;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES, initialDelay = 0L)
    public void run() {
        log.info("KleinanzeigenJob started");
        List<Gesuch> gesuchList = gesuchReaderService.readFromFile("gesuchList.json");

        for (Gesuch gesuch : gesuchList) {
            kleinanzeigenService.fetchListingsAndMessageOnNew(gesuch);
        }
    }
}
