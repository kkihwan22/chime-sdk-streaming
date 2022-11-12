package me.taling.live.core.usecase.live;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.CreateMeetingRequest;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceParameter;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.global.vo.BotEnvironmentKey;
import me.taling.live.global.vo.LiveType;
import me.taling.live.infra.aws.ecs.BotAdapter;
import me.taling.live.infra.aws.ivs.IvsClient;
import me.taling.live.infra.feigns.bot.BotTalingAccountTokenClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class CreateLiveProcessor {
    private final Logger log = LoggerFactory.getLogger(CreateLiveProcessor.class);
    private final SpaceComponent spaceComponent;
    private final AmazonChime amazonChime;

    private final IvsClient ivsClient;
    private final BotAdapter botAdapter;
    private final BotTalingAccountTokenClient botTokenClient;

    @Value("${internal-endpoints.taling-live-front}")
    private String liveFrontHost;
    @Value("${services.reference.live-recorder.app-key}")
    private String appKey;
    @Value("${services.reference.live-recorder.secret-key}")
    private String secretKey;
    @Value("${feign.message.client-id}")
    private String messageClientId;
    @Value("${feign.message.client-secret}")
    private String messageClientSecret;

    public Live process(Live live) {
        log.debug("process live:{}", live);
        Space space = spaceComponent.post(
                new SpaceParameter(live.getId(), "MEETING", live.getTitle(), live.getTutor(), Collections.emptyList(),
                        Collections.emptyList(), live.getType() == LiveType.MEETING ? Boolean.FALSE : Boolean.TRUE));
        log.debug("space:{}", space);
        live.withChannelId(space.getChannelId());

        String meetingId = amazonChime.createMeeting(new CreateMeetingRequest().withExternalMeetingId(live.getTitle()))
                .getMeeting()
                .getMeetingId();
        log.debug("meetingId:{}", meetingId);
        live.withMeetingId(meetingId);
        String botTalingAccountToken = botTokenClient
                .token(BotTalingAccountTokenClient.Request.of())
                .getAccessToken(); // todo redis 캐싱 1시간
        live.withIvs(ivsClient.createChannel(live.getId(), live.getRecordingCondition()));
        live.withEcsTaskId(botAdapter.join(live.getId(), makeBotEnvironmentVariables(live, botTalingAccountToken)));
        return live;
    }

	// todo 중복제거
    private Map<BotEnvironmentKey, String> makeBotEnvironmentVariables(Live live, String token) {
        String liveUrl =
                liveFrontHost + "/cam/" + live.getId() + "?app=" + appKey + "&secret=" + secretKey + "&token=" + token
                        + "&message_id=" + messageClientId + "&message_secret=" + messageClientSecret;
        String rtmpUrl = "rtmps://" + live.getIvs().getIngestEndpoint() + ":443/app/";
        String streamKey = live.getIvs().getStreamKeyValue();

        Map<BotEnvironmentKey, String> environmentVariables = new EnumMap<>(BotEnvironmentKey.class);
        environmentVariables.put(BotEnvironmentKey.LIVE_URL, liveUrl);
        environmentVariables.put(BotEnvironmentKey.RTMP_URL, rtmpUrl);
        environmentVariables.put(BotEnvironmentKey.STREAM_KEY, streamKey);

        return environmentVariables;
    }
}
