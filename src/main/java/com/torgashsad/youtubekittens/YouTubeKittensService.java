package com.torgashsad.youtubekittens;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service that uses YouTube Data API to get random animal videos from YouTube
 */

public class YouTubeKittensService {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(YouTubeKittensService.class);
    /**
     * The list of adjectives that are randomly added to the YouTube query
     */
    private static final List<String> ADJECTIVES = Arrays.asList("Cute", "Adorable", "Funny", "Charming", "Lovely");
    /**
     * The begging of a YouTube video link
     */
    private static final String YOU_TUBE_URL_BEGINNING = "https://www.youtube.com/watch?v=";
    /**
     * Maximum number of results for YouTube response
     */
    private static final long MAX_RESULTS = 50;
    /**
     * A Random class object for random selection
     */
    private static final Random RANDOM = new Random();

    private final YouTube youtubeService;

    private YouTubeKittensService() {
        try {
            this.youtubeService = YTServiceSupplier.getService();
        } catch (Exception e) {
            LOGGER.error("Error on YouTubeService initialization", e);
            throw new RuntimeException();
        }
    }
    /**
     * A singleton of YouTubeKittensService
     */
    public static final YouTubeKittensService INSTANCE = new YouTubeKittensService();
    // TODO: 24.09.2020 CONTINUE HERE
    /**
     * Запрашивает у YouTube список первых 50 видео c милымы животными по запросу,
     * определённому строкой q, возвращает URL со случайным видео из этого списка
     *
     * @param query строка
     */
    public Optional<String> getRandomAnimalVideoURL(String query) {
        // Define and execute the API request
        YouTube.Search.List request;
        try {
            request = youtubeService.search()
                    .list("snippet");
        } catch (IOException e) {
            LOGGER.error(e);
            return Optional.empty();
        }
        LOGGER.info("YouTube query happened to be:"+ ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size())) + " " + query);
        try {
            SearchListResponse response = request.setMaxResults(MAX_RESULTS) //Количество результатов в ответе
                        .setQ(ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size())) + " " + query) //Поисковой запрос
                        .setType("video") //искать только видео
                        .execute();
            //Вернуть URL c котятами
            return getYouTubeVideoId(response).map(id -> YOU_TUBE_URL_BEGINNING + id);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return Optional.empty();

    }

    //извлекает Id случайного видео в формате String из ответа YouTube форматаSearchListResponse.
    private Optional<String> getYouTubeVideoId(SearchListResponse response) {
        return response.getItems().stream().map(item ->
                item.getId().getVideoId()).skip(RANDOM.nextInt(response.getItems().size())).findAny();
    }
}
