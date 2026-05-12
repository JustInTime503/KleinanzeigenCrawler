package de.mc503.kleinanzeigencrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KleinanzeigenCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KleinanzeigenCrawlerApplication.class, args);
    }
}
