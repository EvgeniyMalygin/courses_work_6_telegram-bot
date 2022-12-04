package pro.sky.telegrambot.body;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chat_id;

    private String message;

    private LocalDateTime dataTimeMessage;

    public NotificationTask(Long id, Long chat_id, String message, LocalDateTime dataTimeMessage) {
        this.id = id;
        this.chat_id = chat_id;
        this.message = message;
        this.dataTimeMessage = dataTimeMessage;
    }

    public NotificationTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDataTimeMessage() {
        return dataTimeMessage;
    }

    public void setDataTimeMessage(LocalDateTime dataTimeMessage) {
        this.dataTimeMessage = dataTimeMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(chat_id, that.chat_id) && Objects.equals(message, that.message) && Objects.equals(dataTimeMessage, that.dataTimeMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, dataTimeMessage);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chat_id=" + chat_id +
                ", message='" + message + '\'' +
                ", dataTimeMessage=" + dataTimeMessage +
                '}';
    }
}
