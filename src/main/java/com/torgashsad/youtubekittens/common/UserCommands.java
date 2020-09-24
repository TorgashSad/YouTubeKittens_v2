package com.torgashsad.youtubekittens.common;

import com.torgashsad.youtubekittens.YouTubeKittensService;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
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
     * Returns the text on the button that corresponds to the name of the request
     */
    @Override
    public String getButtonText() {
        return String.format("Show me %s!", name);
    }

    /**
     * Returns the response that corresponds to the name of the request
     * @return
     */
    @Override
    public String getResponse() {

        return YouTubeKittensService.INSTANCE.getRandomAnimalVideoURL(getName()).get();
    }

    /**
     * Return the name of the request
     */
    @Override
    public String getName() {
        return name;
    }

    public static Stream<Commands> stream() {
        return Stream.of(UserCommands.values());
    }

}
