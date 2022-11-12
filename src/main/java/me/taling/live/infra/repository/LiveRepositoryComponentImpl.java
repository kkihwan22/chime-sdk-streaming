package me.taling.live.infra.repository;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Live;
import me.taling.live.global.entity.LiveEntity;
import me.taling.live.global.entity.MeetingEntity;
import me.taling.live.global.entity.ReservationEntity;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_CHIME_MEETING;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_LIVE;

@RequiredArgsConstructor
@Component
public class LiveRepositoryComponentImpl implements LiveRepositoryComponent {
    private final Logger log = LoggerFactory.getLogger(LiveRepositoryComponentImpl.class);

    private final LiveRedisRepository liveRedisRepository;
    private final MeetingRedisRepository meetingRedisRepository;
    private final ReservationRedisRepository reservationRedisRepository;

    @Override
    public void initLive(Live live) {
        LiveEntity savedLiveEntity = liveRedisRepository.save(new LiveEntity(live));
        log.debug("saved liveEntity:{}", savedLiveEntity);

        MeetingEntity savedMeetingEntity = meetingRedisRepository.save(MeetingEntity.builder()
                .id(live.getMeetingId())
                .liveId(live.getId())
                .build());
        log.debug("saved meetingEntity:{}", savedMeetingEntity);
    }

    @Override
    public void reservedLive(Live live) {
        ReservationEntity savedReservation = reservationRedisRepository.save(new ReservationEntity(live));
        log.debug("saved reservation:{}", savedReservation);
    }

    @Override
    public List<Live> findLives() {
        List<Live> lives = new ArrayList<>();
        Iterable<LiveEntity> all = liveRedisRepository.findAll();
        Iterator<LiveEntity> iterator = all.iterator();
        while (iterator.hasNext()) {
            lives.add(iterator.next().toLive());
        }
        log.debug("lives:{}", lives);
        return lives.stream().sorted(Comparator.comparing(Live::getCreateDatetime).reversed()).collect(Collectors.toList());
    }

    @Override
    public Live findLive(String liveId) {
        log.debug("findBy liveId:{}", liveId);
        return liveRedisRepository.findById(liveId)
                .map(LiveEntity::toLive)
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));
    }

    @Override
    public List<Live> findLivesByStartDatetime(String startDatetime) {
        List<ReservationEntity> results = reservationRedisRepository.findByStartDatetime(startDatetime);
        log.debug("results:{}", results);
        return results.stream().map(ReservationEntity::toLive).collect(Collectors.toList());
    }

    @Override
    public void deleteLive(String liveId) {
        log.debug("deleteBy liveId:{}");
        Live live = liveRedisRepository.findById(liveId)
                .map(LiveEntity::toLive)
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));

        meetingRedisRepository.deleteById(live.getMeetingId());
        // TODO: attendee 모두 exit로 처리 (??)
        liveRedisRepository.deleteById(liveId);
    }

    @Override
    public Live findLiveByMeetingId(String meetingId) {
        log.debug("findBy meetingId:{}", meetingId);
        String liveId = meetingRedisRepository.findById(meetingId)
                .map(MeetingEntity::getLiveId)
                .orElseThrow(() -> new LiveException(NOT_FOUND_CHIME_MEETING));
        log.debug("liveId:{}", liveId);
        return liveRedisRepository.findById(liveId)
                .map(LiveEntity::toLive)
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));
    }
}