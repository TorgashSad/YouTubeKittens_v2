package com.torgashsad.youtubekittens.common;

import java.util.stream.Stream;

/**
 * Contains a list (in enum format) of system commands, available for Telegram Bot
 */

public enum SystemCommands implements Commands {
    HELP("help") {
        public String getResponse() {
            return "This is a bot that sends you random videos with animals according to your preferences.\n" +
                    "Just choose a button and get an animal video specified by the button.\n" +
                    "All videos are random and tend to not repeat, but rarely repetitions occur.\n" +
                    "In that unfortunate case, just click the button once more!\n" +
                    "type /start to restart the bot if something went wrong.\n" +
                    "type /help to get this help again \n" +
                    "Создатель: Антонюк Алексей Витальевич";
        }
    },
    START("start") {
        public String getResponse() {
            return "Hello. This is start message. PRESS A BUTTON TO GET ANIMALS!!!\n" +
                    "Type /help for help";
        }
    };
    private final String name;

    SystemCommands(String name)   {
        this.name = name;
    }

    public String getName() {
        return "/"+name;
    }

    /**
     * @return a stream of values of SystemCommands enum
     */
    public static Stream<Commands> stream() {
        return Stream.of(SystemCommands.values());
    }
}
