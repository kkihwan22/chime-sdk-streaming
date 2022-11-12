package me.taling.live.infra.repository;

import me.taling.live.global.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRedisRepository extends CrudRepository<ReservationEntity, String> {

    List<ReservationEntity> findByStartDatetime(String startDatetime);
}
