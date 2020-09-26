package com.torgashsad.youtubekittens;

import com.torgashsad.youtubekittens.common.Commands;
import com.torgashsad.youtubekittens.common.SystemCommands;
import com.torgashsad.youtubekittens.common.UserCommands;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
/**
 * This class represents a telegram bot that send cute animal videos the user
 * @author AAV
 */
@AllArgsConstructor
public class YouTubeKittensBot extends TelegramLongPollingBot {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(YouTubeKittensBot.class);
    /**
     * Reconnect pause in the case of unsuccessful bot registration
     */
    final private static int RECONNECT_PAUSE = 10000;
    /**
     * General response for an unknown command
     */
    public static final String UNKNOWN_COMMAND_RESPONSE = "Unfortunately, i don't know this command yet :( \n" +
            "Enter /help to see available commands";
    @Getter
    final private String botUsername;
    @Getter
    final private String botToken;

    /**
     * This method is executed every time user sends new message to Telegram Bot
     * @param update an object that contains information about user and his request
     */
    @Override
    public void onUpdateReceived(Update update) {
        //Getting update information
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        LOGGER.info(String.format("User with ID:%d have sent the message: %s", chatId, inputText));
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        //Setting a reply keyboard
        message.setReplyMarkup(getReplyKeyboardMarkUp());
        if (userStringCheck(inputText)) {
            message.setText(getCommand(inputText).getResponse());
        }
        else {message.setText(UNKNOWN_COMMAND_RESPONSE);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error on sending message to Telegram", e);
        }
    }

    /**
     * Telegram specific bot connection method
     */
    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Wasn't able to register the bot. Reconnecting...", e);
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                LOGGER.error(e1);
                return;
            }
            botConnect();
        }
    }

    /**
     * Reads the commands from UserCommands enum and returns corresponding ReplyKeyboardMarkup object,
     * that contains structure and data for user keyboard in the bot
     * @return ReplyKeyboardMarkup object that contains bot user keyboard
     */
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
    /**
     * This method check if the list of available commands contains input text
     * @param inputText input text
     * @return true if input text is present in available commands
     */
    private boolean userStringCheck (String inputText) {
        return getAllCommands()
                .map(Commands::getButtonText)
                .anyMatch(n -> n.equals(inputText));
    }

    /**
     * This method returns an object that implements interface Command defined by inputText string
     * @param inputText input text
     * @return object that implements interface Command
     */
    private Commands getCommand (String inputText) {

        Optional<Commands> optCommand = getAllCommands()
                .filter(command -> command.getButtonText().equals(inputText))
                .findAny();
        return optCommand.orElseThrow();
    }

    /**
     * @return ALL Bot command as a Stream<Commands>
     */
    private Stream<Commands> getAllCommands() {
        return Stream.concat(UserCommands.stream(), SystemCommands.stream());
    }
}
