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


public class YouTubeKittensService {

    private static final Logger logger = LogManager.getLogger(YouTubeKittensService.class);

    private static final List<String> adjs = Arrays.asList("Cute", "Adorable", "Funny", "Charming", "Lovely");
    private static final String YouTubeURLBeginning = "https://www.youtube.com/watch?v=";
    private static final long MaxResults = 50;
    private static final Random rand = new Random();

    private final YouTube youtubeService;

    private YouTubeKittensService() {
        try {
            logger.info("MY POINT 3");
            this.youtubeService = YTServiceSupplier.getService();
        } catch (Exception e) {
            logger.error("Error on YouTubeService initialization", e);
            throw new RuntimeException();
        }
    }

    public static final YouTubeKittensService INSTANCE = new YouTubeKittensService();

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
            logger.error(e);
            return Optional.empty();
        }
        logger.info("YouTube query happened to be:"+adjs.get(rand.nextInt(adjs.size())) + " " + query);
        try {
            SearchListResponse response = request.setMaxResults(MaxResults) //Количество результатов в ответе
                        .setQ(adjs.get(rand.nextInt(adjs.size())) + " " + query) //Поисковой запрос
                        .setType("video") //искать только видео
                        .execute();
            //Вернуть URL c котятами
            return getYouTubeVideoId(response).map(id -> YouTubeURLBeginning + id);
        } catch (IOException e) {
            logger.error(e);
        }
        return Optional.empty();

    }

    //извлекает Id случайного видео в формате String из ответа YouTube форматаSearchListResponse.
    private Optional<String> getYouTubeVideoId(SearchListResponse response) {
        return response.getItems().stream().map(item ->
                item.getId().getVideoId()).skip(rand.nextInt(response.getItems().size())).findAny();
    }
}
