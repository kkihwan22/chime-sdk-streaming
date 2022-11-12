package me.taling.live.core.usecase.tutor;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.DeleteMeetingRequest;
import com.amazonaws.services.chime.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.core.usecase.live.command.LiveExpireComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.global.vo.LiveType;
import me.taling.live.infra.feigns.auth.TalingWebFeignClient;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_MATCHED_TUTOR;

@RequiredArgsConstructor
@Component
public class TutorComponentImpl implements TutorComponent {
    private final Logger log = LoggerFactory.getLogger(TutorComponentImpl.class);
    private final LiveRepositoryComponent liveRepositoryComponent;
    private final AmazonChime amazonChime;
    private final TalingWebFeignClient webFeignClient;
    private final LiveExpireComponent liveExpireComponent;

    private final RedisTemplate redisTemplate;

    @Override
    public void startLive(Live live, User requester) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        stringRedisTemplate.opsForValue().set(live.getId(), "READY");
        // redisTemplate.convertAndSend("tutor-entered", live.getId());
        this.updatePreviewUrl(live);
    }

    @Override
    public void closeLive(Parameter parameter) {
        log.debug("parameter:{}", parameter);
        this.requester(parameter);
        this.findLive(parameter);
        this.throwErrorNotMatchedTutor(parameter);

        try {
            amazonChime.deleteMeeting(new DeleteMeetingRequest().withMeetingId(parameter.getLive().getMeetingId()));
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                log.error("e: {}", e);
            }
        }
        liveExpireComponent.execute(parameter.getLive());
    }

    private void requester(Parameter parameter) {
        User requester = ThreadLocalContextProvider.get();
        log.debug("requester : {}", requester);
        parameter.setRequester(requester);
    }

    private void findLive(Parameter parameter) {
        Live live = liveRepositoryComponent.findLive(parameter.getLiveId());
        log.debug("find live:{}", live);
        parameter.setLive(live);
    }

    private void throwErrorNotMatchedTutor(Parameter parameter) {
        User tutor = parameter.getLive().getTutor();
        if (!tutor.isMatchedId(parameter.getRequester().getId())) {
            log.error("Not matched tutor."); // todo : message 내용 변경 (exception 쪽이랑)
            throw new LiveException(NOT_MATCHED_TUTOR);
        }
    }

    private void updatePreviewUrl(Live live) {
        if (live.getType() == LiveType.LESSON) {
            Ivs ivs = live.getIvs();
            String previewStreamUrl = ivs.getStreamUrl();
            log.debug("previewStreamUrl:{}", previewStreamUrl);
            String replayUrl = "https://";
            String replayOriginalUrl = "https://";
            webFeignClient.startLive(live.getId(), previewStreamUrl, replayUrl, replayOriginalUrl);
        }
    }
}