package me.taling.live.core.usecase.live;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Live;
import me.taling.live.core.usecase.live.command.LiveExpireComponent;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LiveForceCloseComponentImpl implements LiveForceCloseComponent {
    private final Logger log = LoggerFactory.getLogger(LiveForceCloseComponentImpl.class);
    private final LiveRepositoryComponent liveRepositoryComponent;
    private final LiveExpireComponent liveExpireComponent;

    @Override
    public void execute(String liveId) {
        Live live = liveRepositoryComponent.findLive(liveId);
        log.debug("find live:{}", live);
        liveExpireComponent.execute(live);
    }
}