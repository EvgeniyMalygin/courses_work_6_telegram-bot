package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambot.body.NotificationTask;

import java.util.Collection;
import java.util.Optional;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    //Запрос поиска записей по полю "дата и время напоминания" в таблице notification_task
    @Query(value = "SELECT * FROM notification_task  WHERE to_char(data_time_message, 'DD.MM.YYYY HH24:MI') LIKE :data", nativeQuery = true)
    Collection<NotificationTask> find(@Param("data") String data);

    Optional<NotificationTask> findById(Long id);
}
