package com.wemarvel.wemarvel.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemarvel.wemarvel.config.Environment;
import com.wemarvel.wemarvel.model.*;
import com.wemarvel.wemarvel.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class InitDataTask {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    private final String removeSchemaScript = "powershell -Command \"(gc fileName | select -Skip 18) -replace 'INTO public.', " +
            "'INTO ' -replace ([regex]::Escape('SET.*$')), '' | Out-File -encoding ASCII fileName\"";

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private CharacterInComicRepository characterInComicRepository;

    @Autowired
    private CharacterInSeriesRepository characterInSeriesRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @PostConstruct
    public void initData() {
        LOGGER.info("Init data");
        //initMarvelCharacters();
        //initComics();
        //initSeries();
        //updateLatestMarvelCharacters();
        //updateLatestComics();
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("Dumping data");
        try {
            File dumpFile = ResourceUtils.getFile("classpath:dump-data.sh");
            String dumpScript = new String(Files.readAllBytes(dumpFile.toPath()));
            int replaceStart = dumpFile.getAbsolutePath().indexOf("target");
            String dataDumpPath = dumpFile.getAbsolutePath().substring(0, replaceStart) +
                    "src\\main\\resources\\data-postgres.sql";
            dumpScript = dumpScript + dataDumpPath;
            LOGGER.info("Dump script: " + dumpScript);
            Process dumpProcess = Runtime.getRuntime().exec(dumpScript);
            dumpProcess.waitFor();
            LOGGER.info("Removing schema name from dumped data");
            Process removeSchemaProcess = Runtime.getRuntime().exec(removeSchemaScript.replace("fileName", dataDumpPath));
            int exitStatus = removeSchemaProcess.waitFor();
            LOGGER.info("Schema name removal exited with status code: " + exitStatus);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
                        resourceURI, modified, 0.0));
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
                            resourceURI, modified, 0.0));
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
                String comicUrl = result.get("urls").get(0).get("url").asText();
                String[] seriesUriSplit = result.get("series").get("resourceURI").asText().split("/");
                Long seriesId = Long.parseLong(seriesUriSplit[seriesUriSplit.length - 1]);
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                comicRepository.save(new Comic(id, seriesId, title, description, variantDescription, thumbnail, comicUrl,
                        modified, format, pageCount, issueNumber, 0.0));
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
                    String comicUrl = result.get("urls").get(0).get("url").asText();
                    String[] seriesUriSplit = result.get("series").get("resourceURI").asText().split("/");
                    Long seriesId = Long.parseLong(seriesUriSplit[seriesUriSplit.length - 1]);
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    comicRepository.save(new Comic(id, seriesId, title, description, variantDescription, thumbnail, comicUrl,
                            modified, format, pageCount, issueNumber, 0.0));
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
                String characterUrl = result.get("urls").get(0).get("url").asText();
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
                        characterUrl, modified, 0.0));
                int availableComics = result.get("comics").get("available").asInt();
                if(availableComics > 0) {
                    for(JsonNode item : result.get("comics").get("items")){
                        String[] resourceUriSplit = item.get("resourceURI").asText().split("/");
                        Long comicId = Long.parseLong(resourceUriSplit[resourceUriSplit.length - 1]);
                        CharacterInComic characterInComic = characterInComicRepository.getByCharacterIdAndComicId(id, comicId);
                        if(characterInComic == null)
                            characterInComicRepository.save(new CharacterInComic(null, id, comicId));
                    }
                }
                if(availableComics > 20) {
                    saveCharacterInComic(id, availableComics);
                }
                int availableSeries = result.get("series").get("available").asInt();
                if(availableSeries > 0) {
                    for(JsonNode item : result.get("series").get("items")){
                        String[] resourceUriSplit = item.get("resourceURI").asText().split("/");
                        Long seriesId = Long.parseLong(resourceUriSplit[resourceUriSplit.length - 1]);
                        CharacterInSeries characterInSeries = characterInSeriesRepository.getByCharacterIdAndSeriesId(id, seriesId);
                        if(characterInSeries == null)
                            characterInSeriesRepository.save(new CharacterInSeries(null, id, seriesId));
                    }
                }
                if(availableSeries > 20) {
                    saveCharacterInSeries(id, availableSeries);
                }
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
                    String characterUrl = result.get("urls").get(0).get("url").asText();
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
                            characterUrl, modified, 0.0));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCharacterInSeries(Long id, int availableSeries) throws JsonProcessingException {
        int offset = 20;
        while (availableSeries > 0) {
            long ts = System.currentTimeMillis();
            String url = Environment.MARVEL_API_URL + "characters/" + id + "/series?limit=100&offset=" + offset +
                    "&ts=" + ts + "&apikey=" + Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
            ResponseEntity<String> response =  new RestTemplate().getForEntity(url, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            JsonNode results = root.get("data").get("results");
            for (JsonNode resultItem : results) {
                Long seriesId = resultItem.get("id").asLong();
                CharacterInSeries characterInSeries = characterInSeriesRepository.getByCharacterIdAndSeriesId(id, seriesId);
                if(characterInSeries == null)
                    characterInSeriesRepository.save(new CharacterInSeries(null, id, seriesId));
            }
            availableSeries = availableSeries - 100;
            offset = offset + 100;
        }
    }

    private void saveCharacterInComic(Long id, int availableComics) throws JsonProcessingException {
        int offset = 20;
        while (availableComics > 0) {
            long ts = System.currentTimeMillis();
            String url = Environment.MARVEL_API_URL + "characters/" + id + "/comics?limit=100&offset=" + offset +
                    "&ts=" + ts + "&apikey=" + Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
            ResponseEntity<String> response =  new RestTemplate().getForEntity(url, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            JsonNode results = root.get("data").get("results");
            for (JsonNode resultItem : results) {
                Long comicId = resultItem.get("id").asLong();
                CharacterInComic characterInComic = characterInComicRepository.getByCharacterIdAndComicId(id, comicId);
                if(characterInComic == null)
                    characterInComicRepository.save(new CharacterInComic(null, id, comicId));
            }
            availableComics = availableComics - 100;
            offset = offset + 100;
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
                String comicUrl = result.get("urls").get(0).get("url").asText();
                String[] seriesUriSplit = result.get("series").get("resourceURI").asText().split("/");
                Long seriesId = Long.parseLong(seriesUriSplit[seriesUriSplit.length - 1]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                LocalDateTime modified = LocalDateTime.now();
                try {
                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                } catch (Exception e) {
                    LOGGER.error("Error parsing date", e);
                }
                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                        result.get("thumbnail").get("extension").asText();
                comicRepository.save(new Comic(id, seriesId, title, description, variantDescription, thumbnail, comicUrl,
                        modified, format, pageCount, issueNumber, 0.0));
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
                    String comicUrl = result.get("urls").get(0).get("url").asText();
                    String[] seriesUriSplit = result.get("series").get("resourceURI").asText().split("/");
                    Long seriesId = Long.parseLong(seriesUriSplit[seriesUriSplit.length - 1]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    comicRepository.save(new Comic(id, seriesId, title, description, variantDescription, thumbnail, comicUrl,
                            modified, format, pageCount, issueNumber, 0.0));
                }
                total = total - 100;
                offset = offset + 100;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSeries() {
        RestTemplate restTemplate = new RestTemplate();
        long ts = System.currentTimeMillis();
        String url = Environment.MARVEL_API_URL + "series?limit=100&ts=" + ts + "&apikey=" +
                Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        int total = 100;
        try {
//            JsonNode root = mapper.readTree(response.getBody());
//            JsonNode data = root.get("data");
//            total = data.get("total").asInt();
//            JsonNode results = data.get("results");
//            for (JsonNode result : results) {
//                Long id = result.get("id").asLong();
//                String title = result.get("title").asText();
//                String type = result.get("type").asText();
//                String description = result.get("description").asText();
//                String seriesUrl = result.get("urls").get(0).get("url").asText();
//                Long startYear = result.get("startYear").asLong();
//                Long endYear = result.get("endYear").asLong();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
//                LocalDateTime modified = LocalDateTime.now();
//                try {
//                    modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
//                } catch (Exception e) {
//                    LOGGER.error("Error parsing date", e);
//                }
//                String thumbnail = result.get("thumbnail").get("path").asText() + "." +
//                        result.get("thumbnail").get("extension").asText();
//                seriesRepository.save(new Series(id, title, description, thumbnail,
//                        seriesUrl, modified, type, startYear, endYear, 0.0));
//            }
//            total = total - 100;
            int offset = 13000;
            while (total > 0) {
                ts = System.currentTimeMillis();
                url = Environment.MARVEL_API_URL + "series?limit=100&offset=" + offset + "&ts=" + ts + "&apikey=" +
                        Environment.MARVEL_API_PUBLIC_KEY + "&hash=" + getHash(ts);
                response = restTemplate.getForEntity(url, String.class);
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode data = root.get("data");
                JsonNode results = data.get("results");
                for (JsonNode result : results) {
                    Long id = result.get("id").asLong();
                    String title = result.get("title").asText();
                    String type = result.get("type").asText();
                    String description = result.get("description").asText();
                    String seriesUrl = result.get("urls").get(0).get("url").asText();
                    Long startYear = result.get("startYear").asLong();
                    Long endYear = result.get("endYear").asLong();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime modified = LocalDateTime.now();
                    try {
                        modified = LocalDateTime.parse(result.get("modified").asText(), formatter);
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date", e);
                    }
                    String thumbnail = result.get("thumbnail").get("path").asText() + "." +
                            result.get("thumbnail").get("extension").asText();
                    seriesRepository.save(new Series(id, title, description, thumbnail,
                            seriesUrl, modified, type, startYear, endYear, 0.0));
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
        //updateLatestMarvelCharacters();
        //updateLatestComics();
    }
}
