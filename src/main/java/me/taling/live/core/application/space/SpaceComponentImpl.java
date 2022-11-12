package me.taling.live.core.application.space;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.core.domain.Space;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.infra.feigns.space.SpaceFeignClient;
import me.taling.live.infra.feigns.space.SpaceFeignRequest;
import me.taling.live.infra.feigns.space.SpaceFeignResponse;
import me.taling.live.infra.feigns.space.SpaceUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.UNKNOWN_ERROR_MESSAGE;

@RequiredArgsConstructor
@Component
public class SpaceComponentImpl implements SpaceComponent {
    private final Logger log = LoggerFactory.getLogger(SpaceComponentImpl.class);
    private final RedisTemplate pubRedisTemplate;
    private final ObjectMapper objectMapper;
    private final SpaceFeignClient spaceFeignClient;

    private final static String SPACE_CHANNEL = "taling_system";

    @Override
    public Space post(SpaceParameter parameter) {
        SuccessResponseWrapper<SpaceFeignResponse> wrapper = spaceFeignClient.postChannel(new SpaceFeignRequest(parameter));
        log.debug("wrapper:{}", wrapper);

        assertThrow(wrapper);
        return new Space(wrapper.getData().getId());
    }

    @Override
    public Space put(String channelId, SpaceParameter parameter) {
        SuccessResponseWrapper<SpaceFeignResponse> wrapper = spaceFeignClient.putChannel(channelId, new SpaceFeignRequest(parameter));
        log.debug("wrapper:{}", wrapper);

        assertThrow(wrapper);
        return new Space(wrapper.getData().getId());
    }

    @Override
    public Space findOne(String channelId) {
        SuccessResponseWrapper<SpaceFeignResponse> wrapper = spaceFeignClient.getChannel(channelId);
        log.debug("wrapper:{}", wrapper);

        assertThrow(wrapper);
        return wrapper.getData().toSpace();
    }

    @Override
    public SpaceUser getUser(String channelId, Long userId) {
        SpaceUser user = spaceFeignClient.getUser(channelId, userId)
                .getData()
                .of();
        log.debug("Get User:{}", user);
        return user;
    }

    @Override
    public void patchUser(String channelId, Long userId, SpaceUser spaceUser) {
        spaceFeignClient.patchUser(channelId, userId, new SpaceUserRequest(spaceUser));
    }

    @Override
    public void publish(SpaceBody body) {
        try {
            pubRedisTemplate.convertAndSend(SPACE_CHANNEL, objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            log.error("An error occurred during JSON parsing.");
            throw new RuntimeException(e.getCause()); // TODO : 500 Exception 정의
        }
    }

    private void assertThrow(SuccessResponseWrapper wrapper) {
        if (wrapper == null || wrapper.getResult().equals("failure")) {
            log.error("Error space response.");
            throw new LiveException(UNKNOWN_ERROR_MESSAGE);
        }
    }
}