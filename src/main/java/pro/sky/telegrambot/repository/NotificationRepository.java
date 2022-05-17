package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;

import java.util.Collection;

public interface NotificationRepository extends JpaRepository<NotificationTask, Long> {

    @Query(value = "SELECT * FROM notification_task where notification_date <= CURRENT_TIMESTAMP and status = 0", nativeQuery = true)
    Collection<NotificationTask> getProcessNotification();
}
