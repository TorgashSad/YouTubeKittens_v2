package com.torgashsad.youtubekittens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class YouTubeKittensApp {

    private static final Logger logger = LogManager.getLogger(YouTubeKittensApp.class);

    public static void main(String[] args) {
        //Получаем серсис YouTube
        //YouTubeKittensService MyService = new YouTubeKittensService(YTServiceSupplier.getService());
        ApiContextInitializer.init(); //Initialization of ApiContext
        //Читаем файл настроек, в которых хранится Telegram Bot Token
        FileInputStream fis;
        Properties property = new Properties();
        String telegramBotToken = null;
        try {
            fis = new FileInputStream("config.properties");
            property.load(fis);
            telegramBotToken = property.getProperty("TelegramBotToken");
        } catch (IOException e) {
            logger.error("Error: properties file is absent!", e);
        }
        YouTubeKittensBot YouTubeKittensBot = new YouTubeKittensBot("YouTubeKittensBot", telegramBotToken);
        YouTubeKittensBot.botConnect();
        logger.info("MY POINT 2");
    }
}