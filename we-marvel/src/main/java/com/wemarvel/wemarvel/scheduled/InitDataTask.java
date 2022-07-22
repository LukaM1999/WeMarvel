package com.wemarvel.wemarvel.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemarvel.wemarvel.config.Environment;
import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.repository.ComicRepository;
import com.wemarvel.wemarvel.repository.CharacterRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class InitDataTask {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ComicRepository comicRepository;

    @PostConstruct
    public void initData() {
        LOGGER.info("Init data");
        //initMarvelCharacters();
        //initComics();
        //updateLatestMarvelCharacters();
        //updateLatestComics();
    }

    private void updateLatestMarvelCharacters(){
        RestTemplate restTemplate = new RestTemplate();
        long ts = System.currentTimeMillis();
        String url = Environment.MARVEL_API_URL + "characters?limit=100&ts=" + ts + "&apikey=" +
                Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts) + "&orderBy=-modified";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        int total = 0;
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");
            total = data.get("total").asInt();
            JsonNode results = data.get("results");
            for (JsonNode result : results) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime modified = LocalDateTime.now();
                try {
                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    if(!modified.isAfter(LocalDateTime.now().minusDays(1).withHour(4))) return;
                } catch (Exception e) {
                    LOGGER.error("Error parsing date", e);
                }
                Long id = result.get("id").asLong();
                String name = result.get("name").asText();
                String description = result.get("description").asText();
                String resourceURI = result.get("resourceURI").asText();
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                characterRepository.save(new MarvelCharacter(id, name, description, thumbnail,
                        resourceURI, modified));
            }
            total = total - 100;
            int offset = 100;
            while (total > 0) {
                ts = System.currentTimeMillis();
                url = Environment.MARVEL_API_URL + "characters?limit=100&offset=" + offset + "&ts=" + ts + "&apikey=" +
                        Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts) + "&orderBy=-modified";
                response = restTemplate.getForEntity(url, String.class);
                root = mapper.readTree(response.getBody());
                data = root.get("data");
                results = data.get("results");
                for (JsonNode result : results) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                        if(!modified.isAfter(LocalDateTime.now().minusDays(1).withHour(4))) return;
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    Long id = result.get("id").asLong();
                    String name = result.get("name").asText();
                    String description = result.get("description").asText();
                    String resourceURI = result.get("resourceURI").asText();
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    characterRepository.save(new MarvelCharacter(id, name, description, thumbnail,
                            resourceURI, modified));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLatestComics(){
        RestTemplate restTemplate = new RestTemplate();
        long ts = System.currentTimeMillis();
        String url = Environment.MARVEL_API_URL + "comics?limit=100&ts=" + ts + "&apikey=" +
                Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts) + "&orderBy=-modified";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        int total = 0;
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");
            total = data.get("total").asInt();
            JsonNode results = data.get("results");
            for (JsonNode result : results) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime modified = LocalDateTime.now();
                try {
                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    if(!modified.isAfter(LocalDateTime.now().minusDays(1).withHour(4))) return;
                } catch (Exception e) {
                    LOGGER.error("Error parsing date", e);
                }
                Long id = result.get("id").asLong();
                String title = result.get("title").asText();
                Double issueNumber = result.get("issueNumber").asDouble();
                String variantDescription = result.get("variantDescription").asText();
                String format = result.get("format").asText();
                int pageCount = result.get("pageCount").asInt();
                String description = result.get("description").asText();
                String resourceURI = result.get("resourceURI").asText();
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                comicRepository.save(new Comic(id, title, description, variantDescription, thumbnail, resourceURI,
                        modified, format, pageCount, issueNumber));
            }
            total = total - 100;
            int offset = 100;
            while (total > 0) {
                ts = System.currentTimeMillis();
                url = Environment.MARVEL_API_URL + "comics?limit=100&offset=" + offset + "&ts=" + ts + "&apikey=" +
                        Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts) + "&orderBy=-modified";
                response = restTemplate.getForEntity(url, String.class);
                root = mapper.readTree(response.getBody());
                data = root.get("data");
                results = data.get("results");
                for (JsonNode result : results) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                        if(!modified.isAfter(LocalDateTime.now().minusDays(1).withHour(4))) return;
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    Long id = result.get("id").asLong();
                    String title = result.get("title").asText();
                    Double issueNumber = result.get("issueNumber").asDouble();
                    String variantDescription = result.get("variantDescription").asText();
                    String format = result.get("format").asText();
                    int pageCount = result.get("pageCount").asInt();
                    String description = result.get("description").asText();
                    String resourceURI = result.get("resourceURI").asText();
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    comicRepository.save(new Comic(id, title, description, variantDescription, thumbnail, resourceURI,
                            modified, format, pageCount, issueNumber));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initMarvelCharacters() {
        RestTemplate restTemplate = new RestTemplate();
        long ts = System.currentTimeMillis();
        String url = Environment.MARVEL_API_URL + "characters?limit=100&ts=" + ts + "&apikey=" +
                Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        int total = 0;
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");
            total = data.get("total").asInt();
            JsonNode results = data.get("results");
            for (JsonNode result : results) {
                Long id = result.get("id").asLong();
                String name = result.get("name").asText();
                String description = result.get("description").asText();
                String resourceURI = result.get("resourceURI").asText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime modified = LocalDateTime.now();
                try {
                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                } catch (Exception e) {
                    LOGGER.error("Error parsing date", e);
                }
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                characterRepository.save(new MarvelCharacter(id, name, description, thumbnail,
                        resourceURI, modified));
            }
            total = total - 100;
            int offset = 100;
            while (total > 0) {
                ts = System.currentTimeMillis();
                url = Environment.MARVEL_API_URL + "characters?limit=100&offset=" + offset + "&ts=" + ts + "&apikey=" +
                        Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
                response = restTemplate.getForEntity(url, String.class);
                root = mapper.readTree(response.getBody());
                data = root.get("data");
                results = data.get("results");
                for (JsonNode result : results) {
                    Long id = result.get("id").asLong();
                    String name = result.get("name").asText();
                    String description = result.get("description").asText();
                    String resourceURI = result.get("resourceURI").asText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    characterRepository.save(new MarvelCharacter(id, name, description, thumbnail,
                            resourceURI, modified));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initComics() {
        RestTemplate restTemplate = new RestTemplate();
        long ts = System.currentTimeMillis();
        String url = Environment.MARVEL_API_URL + "comics?limit=100&ts=" + ts + "&apikey=" +
                Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        int total = 0;
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");
            total = data.get("total").asInt();
            JsonNode results = data.get("results");
            for (JsonNode result : results) {
                Long id = result.get("id").asLong();
                String title = result.get("title").asText();
                Double issueNumber = result.get("issueNumber").asDouble();
                String variantDescription = result.get("variantDescription").asText();
                String format = result.get("format").asText();
                int pageCount = result.get("pageCount").asInt();
                String description = result.get("description").asText();
                String resourceURI = result.get("resourceURI").asText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime modified = LocalDateTime.now();
                try {
                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                } catch (Exception e) {
                    LOGGER.error("Error parsing date", e);
                }
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                comicRepository.save(new Comic(id, title, description, variantDescription, thumbnail, resourceURI,
                        modified, format, pageCount, issueNumber));
            }
            total = total - 100;
            int offset = 100;
            while (total > 0) {
                ts = System.currentTimeMillis();
                url = Environment.MARVEL_API_URL + "comics?limit=100&offset=" + offset + "&ts=" + ts + "&apikey=" +
                        Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
                response = restTemplate.getForEntity(url, String.class);
                root = mapper.readTree(response.getBody());
                data = root.get("data");
                results = data.get("results");
                for (JsonNode result : results) {
                    Long id = result.get("id").asLong();
                    String title = result.get("title").asText();
                    Double issueNumber = result.get("issueNumber").asDouble();
                    String variantDescription = result.get("variantDescription").asText();
                    String format = result.get("format").asText();
                    int pageCount = result.get("pageCount").asInt();
                    String description = result.get("description").asText();
                    String resourceURI = result.get("resourceURI").asText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    comicRepository.save(new Comic(id, title, description, variantDescription, thumbnail, resourceURI,
                            modified, format, pageCount, issueNumber));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHash(long ts) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String toHash = ts + Environment.MARVEL_API_PRIVATE_KEY + Environment.MARVEL_API_PUBLIC_KEY;
        md.update(toHash.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "Europe/Belgrade")
    public void updateDataScheduled(){
        LOGGER.info("Init data scheduled");
        updateLatestMarvelCharacters();
        updateLatestComics();
    }
}
