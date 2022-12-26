package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.body.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final NotificationTaskService notificationTaskService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        Pattern patternData = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            // Process your updates here
            String receivedMessage = update.message().text();
            Matcher matcher = patternData.matcher(receivedMessage);
            Long chatId = update.message().chat().id();

            //обработка принятых сообщений
            if (receivedMessage.equals("/start")) {
                String messageText = "Привет, " + update.message().chat().firstName() + ", Я рад встрече с тобой! " +
                        "Для создания напоминания пришли мне сообщение в формате: 01.12.2022 20:00 Купить корм для собаки";
                sendAnswer(messageText, chatId);
            } else {
                if (matcher.matches()) {
                    String dateTime = matcher.group(1);
                    String messageFromTelegram = matcher.group(3);
                    sendAnswer(notificationTaskService.saveNotification(dateTime, messageFromTelegram, chatId), chatId);
                } else {
                    sendAnswer("Неправильный формат сообщения! Пожалуйста, попробуйте ещё раз.", chatId);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    //Проверка и вывод сообщений из базы
@Scheduled(cron = "0 0/1 * * * *")
    public void checkingRecords() {
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDateTime = currentDateTime.format(formatter) + '%';
        ArrayList<NotificationTask> tempList = new ArrayList<>(notificationTaskService.getListOfDate(formattedDateTime));
        if(!tempList.isEmpty()){
            tempList.forEach(temp -> {
                sendAnswer(temp.getMessage(), temp.getChat_id());
            });
        }
    }


    //отправка ответа в чат
    public void sendAnswer(String messageText, Long chatId) {
        SendMessage message = new SendMessage(chatId, messageText);
        SendResponse response = telegramBot.execute(message);
    }

}
