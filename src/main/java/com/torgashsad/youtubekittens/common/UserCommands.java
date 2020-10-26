package com.torgashsad.youtubekittens.common;

import com.torgashsad.youtubekittens.SpringConfig;
import com.torgashsad.youtubekittens.YouTubeKittensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

/**
 * Contains a list (in enum format) of non-system commands, available for Telegram Bot
 * At the moment (24.09.2020), consists of animal names
 */

public enum UserCommands implements Commands {
    HAMSTERS("hamsters"),
    KITTENS("kittens"),
    PARROTS("parrots"),
    PUPPIES("puppies");

    private final String name;

    public static class StaticYouTubeKittensService {
        static AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        static YouTubeKittensService youTubeKittensService
                = context.getBean("youTubeKittensService", YouTubeKittensService.class);
    }

    UserCommands(String name)  {
        this.name = name;
    }

    /**
     * @return the text on the button in a specific format that corresponds to the name of the request
     */
    @Override
    public String getButtonText() {
        return String.format("Show me %s!", name);
    }

    /**
     * @return the response that corresponds to the name of the request
     * Uses a singleton of YouTubeKittensService class in order to get the response
     */
    public String getResponse() {
        return StaticYouTubeKittensService
                .youTubeKittensService
                .getRandomAnimalVideoURL(getName())
                .orElseThrow();
    }

    public String getName() {
        return name;
    }

    /**
     * @return a stream of values of UserCommands enum
     */
    public static Stream<Commands> stream() {
        return Stream.of(UserCommands.values());
    }

}
