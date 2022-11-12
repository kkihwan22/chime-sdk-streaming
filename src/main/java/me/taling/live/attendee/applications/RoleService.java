package me.taling.live.attendee.applications;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceUser;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NO_PERMISSION;

@RequiredArgsConstructor
@Component
public class RoleService implements RoleUsecase {
    private final Logger log = LoggerFactory.getLogger(RoleService.class);
    private final SpaceComponent spaceComponent;
    private final LiveRepositoryComponent liveRepositoryComponent;

    @Override
    public void execute(RoleParameter parameter) {
        Live live = liveRepositoryComponent.findLive(parameter.getLiveId());
        User requester = ThreadLocalContextProvider.get();
        log.debug("live:{}, requester:{}, userId:{}", live.getId(), requester.getId(), parameter.getUserId());

        this.hasRole(live.getChannelId(), requester.getId());

        SpaceUser spaceUser = spaceComponent.getUser(live.getChannelId(), parameter.getUserId());
        RoleStrategyFactory.factory(parameter.getType().name()).updateRole(live, spaceUser);
    }

    private void hasRole(String channelId, Long userId) {
        SpaceUser spaceUser = spaceComponent.getUser(channelId, userId);
        if (spaceUser.getRole() == SpaceUser.Role.USER) {
            log.info("err. reason : has not permission.");
            throw new LiveException(NO_PERMISSION);
        }
    }
}