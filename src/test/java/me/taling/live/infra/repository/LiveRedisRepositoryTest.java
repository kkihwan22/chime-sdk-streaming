package me.taling.live.infra.repository;

import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.entity.LiveEntity;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;
import me.taling.live.global.vo.StreamMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@DataRedisTest
class LiveRedisRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(LiveRedisRepositoryTest.class);
    private final LiveRedisRepository liveRedisRepository;

    @Autowired
    public LiveRedisRepositoryTest(LiveRedisRepository liveRedisRepository) {
        this.liveRedisRepository = liveRedisRepository;
    }


    private final static String channelId = "test-channel";
    private final static String meetingId = "test-meeting";
    private final static String title = "test";

    @Test
    public void createTest() {
        String uk = "1111111111";
        log.debug("uk:{}", uk);
        liveRedisRepository.save(new LiveEntity(Live.builder()
                .id(uk)
                .type(LiveType.INSTANCE)
                .liveMethod(LiveMethod.INTERACTION)
                .streamMethod(StreamMethod.WEBRTC)
                .recordingCondition(Boolean.FALSE)
                .channelId(channelId)
                .meetingId(meetingId)
                .createDatetime(LocalDateTime.now())
                .expireDatetime(LocalDateTime.now())
                .title(title)
                .tutor(User.builder()
                        .id(1L)
                        .nickname("tutor")
                        .imageUrl("")
                        .thumbnailUrl("")
                        .build())
                .tutees(new ArrayList<>())
                .startDatetime(null)
                .build()));

        Assertions.assertNotNull(liveRedisRepository.findById(uk).get());
    }

}