package com.torgashsad.youtubekittens.common;

import java.util.stream.Stream;

/**
 * This interface describes the behaviour of Telegram Bot commands.
 */

public interface Commands {
    /**
     * Returns the text on the button that corresponds to the name of the command
     */
    default String getButtonText() {
        return getName();
    }

    /**
     * Returns the response that corresponds to the name of the command
     * @return
     */
    String getResponse();

    /**
     * Returns the name of the command
     */
    String getName();

    /**
     * Returns a stream of elements
     */
    static Stream<Commands> stream() {
        return null;
    }

}
