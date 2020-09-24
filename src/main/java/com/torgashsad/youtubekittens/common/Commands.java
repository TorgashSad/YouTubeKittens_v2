package com.torgashsad.youtubekittens.common;

/**
 * This interface describes the behaviour of Telegram Bot commands.
 */

public interface Commands {
    /**
     * @return the text on the button that corresponds to the name of the command
     */
    default String getButtonText() {
        return getName();
    }

    /**
     * @return the response that corresponds to the name of the command
     */
    String getResponse();

    /**
     * @return the name of the command
     */
    String getName();
}
