package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

import java.util.Collection;
import java.util.Optional;

public interface NotificationService {
    Optional<NotificationTask> parseMessage(String notificationMessage);

    void createTask(Long chatId, NotificationTask notificationTask);

    Collection<NotificationTask> processNotificationTask();

}
