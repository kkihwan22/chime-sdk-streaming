package me.taling.live.infra.redis;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AttendeeRedisDataproviderImpl implements AttendeeDataprovider {
    private final Logger log = LoggerFactory.getLogger(AttendeeRedisDataproviderImpl.class);
    private final AttendeeRedisRepository attendeeRedisRepository;

    // TODO: Mapper가 필요함.
    // TODO: DataproviderImpl 위치 변경 >>>> Adapt

    @Override
    public Attendee findOne(Live live, User user) {
        return attendeeRedisRepository.findById(AttendeeRedisEntity.makeId(live.getId(), user.getId()))
                .map(entity -> entity.of(live))
                .orElse(null);
    }

    @Override
    public List<Attendee> finds(Live live) {
        return attendeeRedisRepository.findAllByLiveId(live.getId()).stream()
                .map(entity -> entity.of(live))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Attendee attendee) {
        attendeeRedisRepository.save(new AttendeeRedisEntity(attendee));
    }

    @Override
    public Attendee findByLiveIdAndAttendeeId(Live live, String attendeeId) {
        return attendeeRedisRepository.findByLiveIdAndAttendeeId(live.getId(), attendeeId)
                .map(entity -> entity.of(live))
                .orElse(null);
    }

}
