package me.taling.live.infra.repository;

import me.taling.live.global.entity.MeetingEntity;
import org.springframework.data.repository.CrudRepository;

public interface MeetingRedisRepository extends CrudRepository<MeetingEntity, String> {
}
