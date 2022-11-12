package me.taling.live.infra.repository;

import me.taling.live.global.entity.IvsEntity;
import org.springframework.data.repository.CrudRepository;

public interface IvsRedisRepository extends CrudRepository<IvsEntity, String> {
}
