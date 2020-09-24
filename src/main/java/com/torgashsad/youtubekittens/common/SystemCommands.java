package com.torgashsad.youtubekittens.common;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum SystemCommands implements Commands {
    HELP("/help") {
        @Override
        public String getResponse() {
            return "Hello. This is start message. PRESS A BUTTON TO GET ANIMALS!!!";
        }
    },
    START("/start") {
        @Override
        public String getResponse() {
            return "This is a bot that sends you random videos with animals according to your preferences.\n" +
                    "Just choose a button and get an animal video specified by the button.\n" +
                    "All videos are random and tend to not repeat, but rarely repetitions occur.\n" +
                    "In that unfortunate case, just click the button once more!\n" +
                    "type /start to restart the but if something have gone wrong.\n" +
                    "type /help to get this help again";
        }
    };
    private final String name;

    SystemCommands(String name)   {
        this.name = name;
    }

    /**
     * Return the name of the request
     */
    @Override
    public String getName() {
        return name;
    }


    public static Stream<Commands> stream() {
        return Stream.of(SystemCommands.values());
    }
}
