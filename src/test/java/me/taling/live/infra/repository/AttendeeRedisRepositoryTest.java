package me.taling.live.infra.repository;

import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.infra.redis.AttendeeRedisEntity;
import me.taling.live.infra.redis.AttendeeRedisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@DataRedisTest
class AttendeeRedisRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(AttendeeRedisRepositoryTest.class);

    private final LiveRedisRepository liveRedisRepository;
    private final AttendeeRedisRepository attendeeRedisRepository;

    private String liveId = "1111111111";

    @Autowired
    public AttendeeRedisRepositoryTest(LiveRedisRepository liveRedisRepository,
                                       AttendeeRedisRepository attendeeRedisRepository) {
        this.liveRedisRepository = liveRedisRepository;
        this.attendeeRedisRepository = attendeeRedisRepository;
    }

    @Test
    public void enter() {

        new LiveRedisRepositoryTest(liveRedisRepository).createTest();
        Attendee attendee1 = Attendee.builder()
                .attendeeType(AttendeeType.TUTEE)
                .nickname("test-nickname")
                .thumbnailUrl("")
                .imageUrl("")
                .attendeeId("testchimeattendee")
                .createdDatetime(LocalDateTime.now())
                //.currentDeviceStatusSet(AttendeeType.TUTEE.getDefaultDeviceStatusSet())
                .build();

        Attendee attendee2 = Attendee.builder()
                .attendeeType(AttendeeType.TUTEE)
                .nickname("test-nickname2")
                .thumbnailUrl("")
                .imageUrl("")
                .attendeeId("testchimeattendee2")
                .createdDatetime(LocalDateTime.now())
                //.currentDeviceStatusSet(AttendeeType.TUTEE.getDefaultDeviceStatusSet())
                .build();

        Map<Long, Attendee> data = new HashMap<>();
        data.put(1L, attendee1);
        data.put(2L, attendee2);
        attendeeRedisRepository.save(new AttendeeRedisEntity(attendee1));

        List<AttendeeRedisEntity> attendees = attendeeRedisRepository.findAllByLiveId(liveId);
        Assertions.assertEquals(2, attendees.size());
        log.debug("end..");
    }

    @Test
    public void byIdAndData() {
        this.enter();
    }

}