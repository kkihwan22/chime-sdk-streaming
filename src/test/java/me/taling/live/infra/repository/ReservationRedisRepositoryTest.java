package me.taling.live.infra.repository;

import me.taling.live.core.domain.Live;
import me.taling.live.global.entity.ReservationEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataRedisTest
class ReservationRedisRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(LiveRedisRepositoryTest.class);
    private final ReservationRedisRepository reservationRedisRepository;

    @Autowired
    public ReservationRedisRepositoryTest(ReservationRedisRepository reservationRedisRepository) {
        this.reservationRedisRepository = reservationRedisRepository;
    }

    @Test
    public void findByStartDatetime_ok() {
        LocalDateTime now = LocalDateTime.now();
        ReservationEntity entity1 = new ReservationEntity(Live.builder()
                .id("123456")
                .title("test1")
                .startDatetime(now)
                .build());

        ReservationEntity entity2 = new ReservationEntity(Live.builder()
                .id("123478")
                .title("test2")
                .startDatetime(now)
                .build());


        reservationRedisRepository.save(entity1);
        reservationRedisRepository.save(entity2);
        List<ReservationEntity> result = reservationRedisRepository.findByStartDatetime(now.format(Live.getLiveDatetimeFormatter()));
        log.debug("result:{}", result);
        Assertions.assertEquals(2, result.size());
    }
}