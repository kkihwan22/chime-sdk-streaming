package me.taling.live.infra.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AttendeeRedisRepository extends CrudRepository<AttendeeRedisEntity, String> {

    Optional<AttendeeRedisEntity> findByLiveIdAndAttendeeId(String liveId, String attendeeId);
    List<AttendeeRedisEntity> findAllByLiveId(String liveId);
}
