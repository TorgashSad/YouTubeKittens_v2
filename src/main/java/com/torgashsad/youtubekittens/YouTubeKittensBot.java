package com.torgashsad.youtubekittens;

import com.torgashsad.youtubekittens.common.Commands;
import com.torgashsad.youtubekittens.common.SystemCommands;
import com.torgashsad.youtubekittens.common.UserCommands;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class YouTubeKittensBot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger(YouTubeKittensBot.class);
    final private static int RECONNECT_PAUSE = 10000;
    final private String userName;
    final private String token;


    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        System.out.println(util.userStringCheck(inputText));
        if (util.userStringCheck(inputText)) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setReplyMarkup(getReplyKeyboardMarkUp());
            message.setText(util.getCommand(inputText).getResponse());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                logger.error("Error on sending message to Telegram", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);

        } catch (TelegramApiRequestException e) {
            logger.error("Wasn't able to register the bot", e);
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                logger.error(e1);
                return;
            }
            botConnect();
        }
    }

    // Принимает список строк, возвращает объект типа ReplyKeyboardMarkup,
    // представляющий из себя клавиатуру с кнопками, ссответствующими списку строк
    private ReplyKeyboardMarkup getReplyKeyboardMarkUp() {
        KeyboardRow key = new KeyboardRow();
        key.addAll(UserCommands.stream().map(Commands::getButtonText).collect(Collectors.toList()));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(key);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    //Вспомогательные методы
    public static class util {

        /**
         * Данный метод проверяет, содержится ли сообщение пользователя в списке достпуных команд.
         * Принимает строку, введённую пользователем
         * Возвращает true если содержится, иначе false
         */
        public static boolean userStringCheck (String inputText) {
            return Stream.concat(UserCommands.stream(), SystemCommands.stream()).map(Commands::getButtonText).anyMatch(n -> n.equals(inputText));
        }

        /**
         * Данный метод возвращает объект, реализующий интерфейс Command, соответствующий строке inputText.
         */
        public static Commands getCommand (String inputText) {

            Optional<Commands> optCommand=Stream.concat(UserCommands.stream(), SystemCommands.stream())
                    .filter(d -> d.getButtonText().equals(inputText))
                    .findAny();
            return optCommand.orElseThrow();
        }
    }
}
