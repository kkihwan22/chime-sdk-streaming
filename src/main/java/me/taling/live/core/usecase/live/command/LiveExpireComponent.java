package me.taling.live.core.usecase.live.command;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.global.vo.LiveType;
import me.taling.live.infra.aws.ecs.BotAdapter;
import me.taling.live.infra.aws.ivs.IvsClient;
import me.taling.live.infra.feigns.auth.TalingWebFeignClient;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class LiveExpireComponent {
    private final Logger log = LoggerFactory.getLogger(LiveExpireComponent.class);
    private final BotAdapter botAdapter;
    private final IvsClient ivsClient;
    private final TalingWebFeignClient webFeignClient;
    private final SpaceComponent spaceComponent;
    private final LiveRepositoryComponent liveRepositoryComponent;

    public void execute(Live live) {
        deleteIvsChannel(live.getIvs());
        stopBot(live.getEcsTaskId());
        spaceComponent.publish(new SpaceBody("attendee_expire", live.getChannelId(), Collections.emptyList()));
        if (LiveType.LESSON == live.getType()) {
            webFeignClient.closeLive(live.getId());
        }
        liveRepositoryComponent.deleteLive(live.getId());
        log.debug("end live clear. liveId:{}", live.getId());
    }

    private void deleteIvsChannel(Ivs ivs) {

        try {
            ivsClient.deleteChannel(ivs);
        } catch (Exception e) {
            // TODO: 명확한 처리
            log.error("error delete ivs. reason. {}", e);
        }
    }

    private void stopBot(String taskId) {
        if (taskId == null || taskId == "") {
            return;
        }

        try {
            log.debug("stop ecs. taskId:{}", taskId);
            botAdapter.stop(taskId);
        } catch (Exception e) {
            // TODO: 명확한 처리
            log.error("error delete bot. reason. {}", e);
        }
    }
}
