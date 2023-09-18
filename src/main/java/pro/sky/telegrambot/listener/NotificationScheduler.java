package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.telegrambot.model.NotificationTask;


public class NotificationScheduler {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public void notificationScheduler(TelegramBot telegramBot, NotificationTask notificationTask) {
        SendResponse response = telegramBot.execute(new SendMessage(
                notificationTask.getChatId(),
                notificationTask.getNoteText()));
        if (response.isOk()) {
            logger.info("Notifications sent");
        } else {
            logger.info("There aren't notifications");
        }
    }
}
