package com.torgashsad.youtubekittens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that implements an application that activates and runs YouTubeKittensBot
 */
public class YouTubeKittensApplication {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(YouTubeKittensApplication.class);
    /**
     * Path/name of the configuration file
     */
    private static final String CONFIGURATION_FILE_NAME = "config.properties";
    /**
     * YouTubeKittens Bot username
     */
    private static final String YOUTUBE_KITTENS_BOT_USERNAME = "YouTubeKittensBot";

    public static void main(String[] args) {
        ApiContextInitializer.init(); //Initialization of ApiContext (Telegram API specific)
        Properties property = readPropertiesFile();
        String telegramBotToken = property.getProperty("telegramBotToken");
        YouTubeKittensBot YouTubeKittensBot = new YouTubeKittensBot(YOUTUBE_KITTENS_BOT_USERNAME, telegramBotToken);
        YouTubeKittensBot.botConnect();
    }

    /**
     * This method reads the configuration file and return a Properties object
     * @return Properties object
     */
    private static Properties readPropertiesFile() {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(YouTubeKittensApplication.CONFIGURATION_FILE_NAME);
            prop = new Properties();
            prop.load(fis);
        } catch(Exception e) {
            LOGGER.error("Error: properties file not found", e);
        } finally {
            try {
                assert fis != null;
                fis.close();
            }
            catch (IOException e){
                LOGGER.error("Error: properties file wasn't closed", e);
            }
        }
        return prop;
    }
}