package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

import java.util.Optional;

public interface NotificationService {
Optional<NotificationTask> parseMessage(String notificationMessage);
}
