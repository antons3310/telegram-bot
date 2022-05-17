package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String REGULAR_PATTERN = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Optional<NotificationTask> parseMessage(String notificationMessage) {
        logger.info("Attempt to parse message started");
        Pattern pattern = Pattern.compile(REGULAR_PATTERN);
        Matcher matcher = pattern.matcher(notificationMessage);

        NotificationTask notificationTask = null;

        try {
            if (matcher.matches()) {
                logger.info("DateTime format SUCSESS. Start parsing message");
                LocalDateTime notificationDateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
                String message = matcher.group(3);
                notificationTask = new NotificationTask(notificationDateTime, message);
            }
        } catch (Exception e) {
            logger.error("Failed to parse message. Incorrect DateTime format in message: " + notificationMessage, e);
        }

        return Optional.ofNullable(notificationTask);
    }

    @Override
    public void createTask(Long chatId, NotificationTask notificationTask) {
        notificationTask.setChatId(chatId);

        NotificationTask newNotificationTask = notificationRepository.save(notificationTask);
        logger.info("Task successfully saved and planed. Task: " + newNotificationTask);

    }

    @Override
    public Collection<NotificationTask> processNotificationTask() {
        logger.info("Star processing notification for send");

        Collection<NotificationTask> notificationTasks = notificationRepository.getProcessNotification();
        logger.info("{} notifications to process", notificationTasks.size());
        for (NotificationTask task :
                notificationTasks) {
            task.updateStatus();
        }

        notificationRepository.saveAll(notificationTasks);
        logger.info("{} notifications updated for send", notificationTasks.size());
        return notificationTasks;
    }
}
