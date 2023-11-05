package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    private HashMap<Long, UserData> users;
    public Bot(){
        users = new HashMap<>();
    }
    private String answer;

    @Override
    public String getBotUsername() {
        return "botyarajuna_bot";
    }

    @Override
    public String getBotToken() {
        return "6391997214:AAFO67IjEt7uTuPMu90snoJ2JsPiLQbuYss";
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long userId = message.getFrom().getId();

        if (text.equals("/start")) {
            sendText(userId, "Привет! Этот бот - странная трата твоего времени, начнем?:)");
            users.put(userId, new UserData());
            String question = getQuestion(1);
            sendText(userId, question);
        } else if (users.get(userId).getQuestionNumber() >= 4) {
            sendText(userId, "Твой рейтинг: " + users.get(userId).getScore() + " из 4");
            sendText(userId, "Для перезапуска печатай команду /start");
        } else {
            UserData userData = users.get(userId);
            int questionNumber = userData.getQuestionNumber();
            boolean result = checkAnswer(questionNumber, text);
            int score = userData.getScore();
            userData.setScore(score +  (result ? 1 : 0 ));
            userData.setQuestionNumber(questionNumber + 1);
            String question = getQuestion(userData.getQuestionNumber());
            sendText(userId, question);

            if (questionNumber == 4){
                sendText(userId, "Твой рейтинг: " + userData.getScore() + " из 4");
            }
        }

    }

    public boolean checkAnswer(int number, String answer) {
        answer = answer.toLowerCase();
        if (number == 1) {
            return answer.equals("60");
        }
        if (number == 2) {
            return answer.equals("5");
        }
        if (number == 3) {
            return answer.contains("пукни в пакетик");
        }
        if (number == 4) {
            return answer.contains("завтрак") &&
                    answer.contains("обед") &&
                    answer.contains("ужин");
        }
        return false;
    }


    private String getQuestion(int questionNumber) {
        if (questionNumber == 1) {
            return "Вопрос 1. Сколько минут в часе?";
        } else if (questionNumber == 2) {
            return "Сколько пальцев на руке?";

        } else if (questionNumber == 3) {
            return "Вопрос 3. Что нужно ответить на Приветик?";
        } else if (questionNumber == 4) {
            return "Вопрос 4. Какие приемы пищи в день вы знаете?";
        }
        return "";
    }
}