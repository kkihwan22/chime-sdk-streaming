package me.taling.live.attendee.applications.strategy;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.applications.RoleStrategy;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.application.space.ActionMessage;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceUser;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_USER;

@RequiredArgsConstructor
@Component
public class ManagerRoleStrategy implements RoleStrategy {
    private final Logger log = LoggerFactory.getLogger(TuteeRoleStrategy.class);
    private final AttendeeDataprovider attendeeDataprovider;
    private final SpaceComponent spaceComponent;

    @Override
    public Attendee updateRole(Live live, SpaceUser spaceUser) {
        spaceUser.changedRoleForManager();
        User user = spaceUser.toUser();
        spaceComponent.patchUser(live.getChannelId(), user.getId(), spaceUser);

        Attendee attendee = attendeeDataprovider.findOne(live, user);
        if (attendee == null) {
            log.error(""); // TODO: 로그 정의하기.
            throw new LiveException(NOT_FOUND_USER);
        }

        attendee.changedRoleForManager(live);
        attendeeDataprovider.save(attendee);
        spaceComponent.publish(new SpaceBody(
                "attendee_action",
                live.getChannelId(),
                new ActionMessage("screen_share_unlock", Collections.singletonList(new AttendeeResponse(attendee)))));

        return attendee;
    }
}