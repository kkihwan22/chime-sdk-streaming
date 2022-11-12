package me.taling.live.core.usecase.live;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.CreateMeetingRequest;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.global.vo.BotEnvironmentKey;
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

import java.util.EnumMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class LessonLiveCreateComponentImpl implements LessonLiveCreateComponent {
    private final Logger log = LoggerFactory.getLogger(LessonLiveCreateComponentImpl.class);
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

    @Override
    public LessonLiveCreateResult execute(LessonLiveCreateParameter parameter) {
        log.debug("parameter:{}", parameter);

        Space space = spaceComponent.findOne(parameter.getChannelId());
        log.debug("find space:{}", space);

        Live live = new Live(parameter.getLiveId())
                .withChannelId(parameter.getChannelId())
                .withType(LiveType.LESSON)
                .withLiveMethod(parameter.getLiveMethod())
                .withStreamMethod(StreamMethod.WEBRTC)
                .withTitle(parameter.getTitle())
                .withRecordingCondition(parameter.getRecCondition())
                .withTutor(space.getTutor())
                .withManagers(space.getManagers())
                .withTutees(space.getTutees())
                .withStartDatetime(parameter.getStartedAt())
                .withEndDatetime(parameter.getExpiredAt())
                .withExpireDatetime(parameter.getExpiredAt().plusHours(2));

        String meetingId = amazonChime.createMeeting(new CreateMeetingRequest().withExternalMeetingId(live.getTitle())).getMeeting().getMeetingId();
        log.debug("meetingId:{}", meetingId);
        live.withMeetingId(meetingId);
        live.withIvs(createIvsChannel(live));
        live.withEcsTaskId(this.getEcsTaskId(live));
        liveRepositoryComponent.initLive(live);

        return null;
    }

    private Ivs createIvsChannel(Live live) {
        log.debug("create ivs channel. live.id:{}, live.recCondition:{}", live.getId(), live.getRecordingCondition());
        Ivs ivs = ivsClient.createChannel(live.getId(), live.getRecordingCondition());
        log.debug("ivs:{}", ivs);
        return ivs;
    }

    private String getEcsTaskId(Live live) {
        String botTalingAccountToken = botTokenClient.token(BotTalingAccountTokenClient.Request.of()).getAccessToken(); // todo redis 캐싱 1시간
        String taskId = botAdapter.join(live.getId(), makeBotEnvironmentVariables(live, botTalingAccountToken));
        log.debug("taskId: {}", taskId);
        return taskId;
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
