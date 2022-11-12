package me.taling.live.attendee.applications;

import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;

public interface BeforeEnterLiveSpaceStrategy {
    AttendeeType execute(Live live, User requester);
}
