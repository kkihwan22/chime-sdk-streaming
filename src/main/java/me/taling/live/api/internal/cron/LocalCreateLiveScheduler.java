package me.taling.live.api.internal.cron;

import lombok.RequiredArgsConstructor;
import me.taling.live.listener.LiveListener.Event;
import me.taling.live.listener.LiveListener.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
@Profile({"local"})
public class LocalCreateLiveScheduler {
    private final static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private final Logger log = LoggerFactory.getLogger(LocalCreateLiveScheduler.class);
    private final ApplicationEventPublisher publisher;

    @Scheduled(cron = "0 * * * * ?")
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("run cron : {}", now);
        Event<String> liveEvent = new Event<>(Type.CREATE_RESERVED_LIVE, now.format(DATETIME_FORMATTER));
        log.debug("liveEvent:{}", liveEvent);
        publisher.publishEvent(liveEvent);
    }
}