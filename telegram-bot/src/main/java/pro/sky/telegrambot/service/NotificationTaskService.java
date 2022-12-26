package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.body.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;


    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }
// Создание записи в таблице notification_task
    public String saveNotification (String dateTime, String messageFromTelegram, Long chatId){

        // Создание сущности для записи в таблицу
        LocalDateTime tempDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        NotificationTask tempNotificationTask = new NotificationTask();
        tempNotificationTask.setChat_id(chatId);
        tempNotificationTask.setDataTimeMessage(tempDateTime);
        tempNotificationTask.setMessage(messageFromTelegram);

        // получение списка записей из базы данных для проверки существования такой же записи как и текущая
        ArrayList<NotificationTask> tempList = new ArrayList<>(getListOfDate(dateTime));

        // проверка на существование записи
        if (tempList.isEmpty() || !tempList.contains(tempNotificationTask)){
            // Запись сущности notification task в таблицу notification_task
            notificationTaskRepository.save(tempNotificationTask);
            return "Напоминание создано";
        }
        return "Такое напоминание уже создавалось";
    }

    public ArrayList<NotificationTask> getListOfDate(String dateTime) {
        dateTime += '%';
        return new ArrayList<>(notificationTaskRepository.find(dateTime));
    }


}
