package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService implements UpdatesListener {
    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            Matcher matcher = pattern.matcher(update.message().text());
            if (matcher.matches()) {
                String dateTime = matcher.group(1);
                String item = matcher.group(3);
                notificationTaskRepository.save(new NotificationTask(
                        update.message().chat().id(),
                        item,
                        LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        List<NotificationTask> notificationTasks = notificationTaskRepository.findByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        NotificationScheduler notificationScheduler = new NotificationScheduler();
        notificationTasks.forEach(
                notificationTask -> {
                    notificationScheduler.notificationScheduler(telegramBot, notificationTask);
                });

    }

}
