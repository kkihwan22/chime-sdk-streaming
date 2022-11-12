package me.taling.live.core.usecase.live;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Ivs;
import me.taling.live.global.entity.LiveEntity;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.infra.repository.LiveRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.*;

@RequiredArgsConstructor
@Component
public class IvsInfoComponentImpl implements IvsInfoComponent{

    private final Logger log = LoggerFactory.getLogger(IvsInfoComponentImpl.class);
    private final LiveRedisRepository liveRedisRepository;

    @Override
    public Ivs ivs(String liveId) {
        log.debug("findBy liveId:{}", liveId);
        return liveRedisRepository.findById(liveId)
                .map(LiveEntity::getIvs)
                .orElseThrow(() -> new LiveException(NOT_FOUND_CHANNEL_BY_IVS));
    }
}
