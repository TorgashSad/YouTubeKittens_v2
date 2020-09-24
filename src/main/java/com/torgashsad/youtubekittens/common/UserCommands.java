package com.torgashsad.youtubekittens.common;

import com.torgashsad.youtubekittens.YouTubeKittensService;

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

        return YouTubeKittensService.INSTANCE.getRandomAnimalVideoURL(getName()).orElseThrow();
    }

    /**
     * @return the name of the request
     */
    public String getName() {
        return name;
    }

    /**
     * @return a stream of values of SystemCommands enum
     */
    public static Stream<Commands> stream() {
        return Stream.of(UserCommands.values());
    }

}
