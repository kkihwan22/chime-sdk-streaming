package me.taling.live.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.Live;
import me.taling.live.core.usecase.live.CreateLiveProcessor;
import me.taling.live.global.exceptions.BadParameterException;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LiveListener {
    private final Logger log = LoggerFactory.getLogger(LiveListener.class);
    private final CreateLiveProcessor createLiveProcessor;
    private final LiveRepositoryComponent liveRepositoryComponent;

    // TODO: 좀 더 SRP하게 변경....
    @Async("eventTaskExecutor")
    @EventListener
    public void listen(Event event) {
        log.debug("event:{}", event);
        switch (event.getType()) {
            case CREATE_RESERVED_LIVE:
                String now = (String) event.getData();
                List<Live> lives = liveRepositoryComponent.findLivesByStartDatetime(now);
                log.debug("lives:{}", lives);
                for (Live live : lives) {
                    liveRepositoryComponent.initLive(createLiveProcessor.process(live));
                }
                break;
            case EXPIRED_MEETING:
                List<String> liveIds = (List<String>) event.getData();
                log.debug("Delete live Ids. {}", liveIds);
                for (String liveId : liveIds) {

                }
                break;
            default:
                throw new BadParameterException("Not matched enum code.[" + event.getType() + "]");
        }

    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class Event<T> {
        private Type type;
        private T data;
    }

    public enum Type {
        CREATE_RESERVED_LIVE, EXPIRED_MEETING,
    }
}
