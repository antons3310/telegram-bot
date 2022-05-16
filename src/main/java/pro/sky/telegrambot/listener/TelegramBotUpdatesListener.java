package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final String START_MESSAGE = "/start";

    private final TelegramBot telegramBot;
    private final NotificationService notificationService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Message message =update.message();

            if (message.text().startsWith(START_MESSAGE)){
                logger.info("start message received");

                SendMessage sendMessage = new SendMessage(getChatId(message), "Привет " + getUserName(message) + ". Я бот-напоминался. Напиши свое сообщение в формате: \"01.01.2022 20:00 Сделать домашнюю работу\" и я напомню в назначенный час о запланированных делах!");

                SendResponse response = telegramBot.execute(sendMessage);
            } else {
                notificationService.parseMessage(update.message().text());
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private Long getChatId(Message message){
        return message.chat().id();
    }

    private String getUserName(Message message){
        return message.from().firstName();
    }
}
