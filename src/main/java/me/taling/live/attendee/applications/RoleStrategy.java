package me.taling.live.attendee.applications;

import me.taling.live.attendee.domain.Attendee;
import me.taling.live.core.application.space.SpaceUser;
import me.taling.live.core.domain.Live;

public interface RoleStrategy {

    Attendee updateRole(Live live, SpaceUser spaceUser);
}
