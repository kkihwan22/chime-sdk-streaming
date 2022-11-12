package me.taling.live.core.application.space;

import me.taling.live.core.domain.Space;

public interface SpaceComponent {
    Space post(SpaceParameter parameter);

    Space put(String channelId, SpaceParameter parameter);

    Space findOne(String channelId);

    SpaceUser getUser(String channelId, Long userId);

    void patchUser(String channelId, Long userId, SpaceUser spaceUser);

    void publish(SpaceBody body);

}