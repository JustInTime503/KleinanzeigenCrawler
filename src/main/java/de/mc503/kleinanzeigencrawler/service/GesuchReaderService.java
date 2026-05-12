package de.mc503.kleinanzeigencrawler.service;

import de.mc503.kleinanzeigencrawler.model.Gesuch;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GesuchReaderService {

    public List<Gesuch> readFromFile(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(String.join("", lines), new TypeReference<List<Gesuch>>(){});
    }
}
