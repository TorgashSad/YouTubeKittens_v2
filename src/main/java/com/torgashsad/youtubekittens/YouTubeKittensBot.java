package com.torgashsad.youtubekittens;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class YouTubeKittensBot extends TelegramLongPollingBot {

    final private static int RECONNECT_PAUSE = 10000;
    final private static List<String> serviceMsgs = Arrays.asList("/start", "/help");
    final private static List<String> animals = Arrays.asList("kittens", "puppies", "parrots", "hamsters");
    final private static List<String> animalKeys = getKeys();
    final private static List<String> allKeys = Stream.concat(serviceMsgs.stream(), animalKeys.stream())
            .collect(Collectors.toList());
    final private static Map<String, String> map = new LinkedHashMap<>();

    final private String userName;

    final private String token;

    final private YouTubeKittensService MyYouTubeKittensService;

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        for (int i=0; i<animals.size(); i++) {
            map.put(animalKeys.get(i), animals.get(i));
        } //создаём отображение animalKeys (ключи) на animals (значения)
        if (util.stringContainsItemFromList(inputText, allKeys)) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setReplyMarkup(getReplyKeyboardMarkUp());
            String reply = getReply(inputText);
            message.setText(reply);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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

            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        }
    }

    // Принимает список строк, возвращает объект типа ReplyKeyboardMarkup,
    // представляющий из себя клавиатуру с кнопками, ссответствующими списку строк
    private ReplyKeyboardMarkup getReplyKeyboardMarkUp() {
        KeyboardRow key = new KeyboardRow();
        key.addAll(animalKeys);
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(key);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    // Преобразует список зверей для поиска в формат ("Show me " + animal + "!")
    // На основании введённой пользователем строки возвращает строку ответа
    private String getReply(String inputText) {
        if (util.stringContainsItemFromList(inputText, animalKeys)) {
            try {
                return MyYouTubeKittensService.getRandomAnimalVideoURL(map.get(inputText));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        //сервисные команды из serviceMsgs
        if (inputText.startsWith("/start")) {
            return "Hello. This is start message. PRESS A BUTTON TO GET ANIMALS!!!";
        }
        if (inputText.startsWith("/help")) {
            return "This is a bot that sends you random videos with animals according to your preferences.\n" +
                    "Just choose a button and get an animal video specified by the button.\n" +
                    "All videos are random and tend to not repeat, but rarely repetitions occur.\n" +
                    "In that unfortunate case, just click the button once more!\n" +
                    "type /start to restart the but if something have gone wrong.\n" +
                    "type /help to get this help again";
        }
        return "I have no answer for you :(";
    }

    // Преобразует список зверей для поиска в формат ("Show me " + animal + "!")
    private static List<String> getKeys() {
        List<String> Keys = new ArrayList<>();
        for (String animal : YouTubeKittensBot.animals) {
            Keys.add("Show me " + animal + "!");
        }
        return Keys;
    }

    //Вспомогательные методы
    public static class util {
        // Проверяет, содержится ли в строке inputStr какой либо элемент из items
        public static boolean stringContainsItemFromList(String inputStr, List<String> items) {
            return items.stream().anyMatch(inputStr::contains);
        }
    }
}
