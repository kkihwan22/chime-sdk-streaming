package me.taling.live.core.usecase.live;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.CreateMeetingRequest;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.global.vo.BotEnvironmentKey;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;
import me.taling.live.global.vo.StreamMethod;
import me.taling.live.infra.aws.ecs.BotAdapter;
import me.taling.live.infra.aws.ivs.IvsClient;
import me.taling.live.infra.feigns.bot.BotTalingAccountTokenClient;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InstanceCreateComponentImpl implements CreateComponent {
    private final Logger log = LoggerFactory.getLogger(InstanceCreateComponentImpl.class);
    private final SpaceComponent spaceComponent;
    private final LiveRepositoryComponent liveRepositoryComponent;
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

    public CreateResult execute(CreateParameter parameter) {
        log.debug("parameter:{}", parameter);

        Space space = spaceComponent.findOne(parameter.getChannelId());
        log.debug("find space:{}", space);

        LocalDateTime now = LocalDateTime.now();
        Live live = new Live(parameter.getLiveId())
                .withChannelId(parameter.getChannelId())
                .withType(LiveType.INSTANCE)
                .withLiveMethod(Optional.ofNullable(parameter.getLiveMethod()).orElse(LiveMethod.INTERACTION))
                .withStreamMethod(StreamMethod.WEBRTC)
                .withTitle(parameter.getTitle())
                .withRecordingCondition(parameter.getRecCondition())
                .withTutor(space.getTutor())
                .withManagers(space.getManagers())
                .withTutees(space.getTutees())
                .withStartDatetime(now)
                .withEndDatetime(now.plusHours(1))
                .withExpireDatetime(now.plusHours(2));

        String meetingId = amazonChime.createMeeting(new CreateMeetingRequest().withExternalMeetingId(live.getTitle())).getMeeting().getMeetingId();
        log.debug("meetingId:{}", meetingId);
        live.withMeetingId(meetingId);
        live.withIvs(createIvsChannel(live));
        live.withEcsTaskId(this.getEcsTaskId(live));
        liveRepositoryComponent.initLive(live);

        return new CreateResult(live);
    }

    private Ivs createIvsChannel(Live live) {
        log.debug("create ivs channel. live.id:{}, live.recCondition:{}", live.getId(), live.getRecordingCondition());
        Ivs ivs = ivsClient.createChannel(live.getId(), live.getRecordingCondition());
        log.debug("ivs:{}", ivs);
        return ivs;
    }

    private String getEcsTaskId(Live live) {
        String botTalingAccountToken = botTokenClient.token(BotTalingAccountTokenClient.Request.of()).getAccessToken(); // todo redis ?????? 1??????
        String taskId = botAdapter.join(live.getId(), makeBotEnvironmentVariables(live, botTalingAccountToken));
        log.debug("taskId: {}", taskId);
        return taskId;
    }

    // todo ????????????
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