package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private LocalDateTime notificationDate;
    private LocalDateTime sentDate;
    private String notificationMessage;

    public enum NotificationStatus {
        PROCESS,
        SENT,
    }

    @Enumerated
    private NotificationStatus status = NotificationStatus.PROCESS;

    public NotificationTask() {
    }

    public NotificationTask(LocalDateTime notificationDate, String notificationMessage) {
        this.notificationDate = notificationDate;
        this.notificationMessage = notificationMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public void updateStatus() {
        this.status = NotificationStatus.SENT;
        this.sentDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask)) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getChatId(), that.getChatId()) && Objects.equals(getNotificationDate(), that.getNotificationDate()) && Objects.equals(getSentDate(), that.getSentDate()) && Objects.equals(getNotificationMessage(), that.getNotificationMessage()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getChatId(), getNotificationDate(), getSentDate(), getNotificationMessage(), getStatus());
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", notificationDate=" + notificationDate +
                ", sentDate=" + sentDate +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", status=" + status +
                '}';
    }
}
