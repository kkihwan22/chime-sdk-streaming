package me.taling.live.infra.repository;

import me.taling.live.global.entity.LiveEntity;
import org.springframework.data.repository.CrudRepository;

public interface LiveRedisRepository extends CrudRepository<LiveEntity, String> {
}
