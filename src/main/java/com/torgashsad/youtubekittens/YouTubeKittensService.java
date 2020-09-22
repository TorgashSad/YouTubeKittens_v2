package com.torgashsad.youtubekittens;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
public class YouTubeKittensService {

    private static final Logger logger = LogManager.getLogger(YouTubeKittensService.class);

    private static final List<String> adjs = Arrays.asList("Cute", "Adorable", "Funny", "Charming", "Lovely");
    private static final String YouTubeURLBeginning = "https://www.youtube.com/watch?v=";
    private static final long MaxResults = 50;
    private static final Random rand = new Random();

    YouTube youtubeService;

    /**
     * Запрашивает у YouTube список первых 50 видео c милымы животными по запросу,
     * определённому строкой q, возвращает URL со случайным видео из этого списка
     *
     * @throws IOException, NotFoundException
     */

    public String getRandomAnimalVideoURL(String q) throws IOException, NotFoundException {
        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list("snippet");
        logger.info("YouTube querry happened to be:"+adjs.get(rand.nextInt(adjs.size())) + " " + q);
        SearchListResponse response = request.setMaxResults(MaxResults) //Количество результатов в ответе
                    .setQ(adjs.get(rand.nextInt(adjs.size())) + " " + q) //Поисковой запрос
                    .setType("video") //искать только видео
                    .execute(); //Отправка запроса и получение ответа в response
        return YouTubeURLBeginning + getYouTubeVideoId(response); //Вернуть URL c котятами

    }

    //извлекает Id случайного видео в формате String из ответа YouTube форматаSearchListResponse.
    //Если по какой-то причине Id не был найден - кидает NotFoundException.
    private static String getYouTubeVideoId(SearchListResponse response) throws NotFoundException {
        Optional<String> Id = response.getItems().stream().map(item ->
                item.getId().getVideoId()).skip(rand.nextInt(response.getItems().size())).findAny();
        if (Id.isPresent()) {
            return Id.get();
        } else throw new NotFoundException("There is no Id found in the response.");
    }
}
