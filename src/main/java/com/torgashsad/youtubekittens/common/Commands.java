package com.torgashsad.youtubekittens.common;

import java.util.Optional;

public interface Commands {
    /**
     * Returns the text on the button that corresponds to the name of the request
     */
    default String getButtonText() {
        return "";
    }

    /**
     * Returns the response that corresponds to the name of the request
     * @return
     */
    Optional<String> getResponse();

    /**
     * Return the name of the request
     */
    String getName();

}
