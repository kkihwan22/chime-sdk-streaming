package me.taling.live.api.internal.cron;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Lives;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.infra.aws.chime.ChimeClient;
import me.taling.live.infra.aws.chime.ChimeClientParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Profile({"local"})
public class LocalDeleteLiveScheduler {
    private final Logger log = LoggerFactory.getLogger(LocalDeleteLiveScheduler.class);
    private final QueryComponent queryComponent;
    private final ChimeClient chimeClient;

    @Scheduled(fixedDelay = 1000 * 60 * 10)
    public void run() {
        List<Lives> list = queryComponent.lives();
        log.debug("list:{}", list);

        for (Lives item : list) {
            try {
                chimeClient.findMeeting(ChimeClientParameter.builder()
                        .liveId(item.getLiveId())
                        .meetingId(item.getMeetingId())
                        .build());
            } catch (Exception e) {
                log.error("e: {}", e);
            }
        }
    }
}