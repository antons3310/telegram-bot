package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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

            Message message = update.message();

            if (message.text().startsWith(START_MESSAGE)) {
                logger.info("start message received");

                sendMessage(getChatId(message), "Привет " + getUserName(message) + ". Я бот-напоминалка. Напиши свое сообщение в формате: \"01.01.2022 20:00 Сделать домашнюю работу\" и я напомню в назначенный час о запланированных делах!");
            } else {
                Optional<NotificationTask> notificationTask = notificationService.parseMessage(update.message().text());

                if (notificationTask.isPresent()) {
                    saveNotification(getChatId(message), notificationTask.get());
                } else {
                    sendMessage(getChatId(message), "Формат собщения не корректный. Попробуйте снова");
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void saveNotification(Long chatId, NotificationTask notificationTask) {
        notificationService.createTask(chatId, notificationTask);
        sendMessage(chatId, "Задание успешно запланировано. Ожидайте уведомления");
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void processNotification() {
        logger.info("Start sending notifications");
        Collection<NotificationTask> notificationTasks = notificationService.processNotificationTask();
        for (NotificationTask task :
                notificationTasks) {
            sendMessage(task.getChatId(), task.getNotificationMessage());
        }
    }

    private Long getChatId(Message message) {
        return message.chat().id();
    }

    private String getUserName(Message message) {
        return message.from().firstName();
    }

    private void sendMessage(Long chatID, String message) {
        SendMessage sendMessage = new SendMessage(chatID, message);
        telegramBot.execute(sendMessage);
    }
}
