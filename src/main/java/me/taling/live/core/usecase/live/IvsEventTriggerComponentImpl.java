package me.taling.live.core.usecase.live;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.taling.live.infra.feigns.auth.TalingWebFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IvsEventTriggerComponentImpl implements IvsEventTriggerComponent {
    private final Logger log = LoggerFactory.getLogger(IvsEventTriggerComponentImpl.class);
    private final TalingWebFeignClient talingWebFeignClient;

    @Value("${internal-endpoints.taling-stream}")
    @Setter
    private String domain;

    @Value("${aws.ivs.origin-source.prefix}")
    @Setter
    private String prefix;

    @Value("${aws.ivs.origin-source.postfix}")
    @Setter
    private String postFix;

    @Override
    public void execute(IvsEventParameter parameter) {
        log.debug("ivs event parameter:{}", parameter);
        if (parameter.getRecordingStatus().equals("Recording End")) {
            talingWebFeignClient.updateLive(
                    parameter.getChannelName(), "0", "0",
                    this.replayOriginalUrl(parameter.getKeyPrefix()),
                    this.replayUrl(parameter.getKeyPrefix())
            );

            // TODO: Lesson 일 때만 처리할 수 있도록 개선...
            /**
             Live live = liveRepositoryComponent.findLive(ivsEvent.channelName);
             log.debug("find Live by channelName:{}", live);

             if (live.getType() == LiveType.LESSON) {
             log.debug("updateLiveClass.");

             }
             */
        }
    }

    private String replayUrl(String src) {
        String replayUrl = new StringBuilder()
                .append(this.domain)
                .append("/")
                .append(src)
                .append(this.postFix)
                .toString();
        log.debug("replayUrl:{}", replayUrl);
        return replayUrl;
    }

    private String replayOriginalUrl(String src) {
        String replayUrl = new StringBuilder()
                .append(this.prefix)
                .append("/")
                .append(src)
                .append(this.postFix)
                .toString();
        log.debug("replayUrl:{}", replayUrl);
        return replayUrl;
    }
}
