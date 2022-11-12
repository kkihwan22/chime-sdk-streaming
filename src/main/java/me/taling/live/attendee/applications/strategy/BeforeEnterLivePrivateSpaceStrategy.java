package me.taling.live.attendee.applications.strategy;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.applications.BeforeEnterLiveSpaceStrategy;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.core.domain.User;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.UN_ACCEPTABLE_USER;

@RequiredArgsConstructor
@Component
public class BeforeEnterLivePrivateSpaceStrategy implements BeforeEnterLiveSpaceStrategy {
    private final Logger log = LoggerFactory.getLogger(BeforeEnterLivePrivateSpaceStrategy.class);
    private final SpaceComponent spaceComponent;

    // TODO: Space profile 조회 (단건 조회, 단건 수정으로 로직 변경이 필요)
    @Override
    public AttendeeType execute(Live live, User requester) {
        Space space = spaceComponent.findOne(live.getChannelId());
        if (space.getTutor().equals(requester)) {
            log.debug("Live:{}, Requester:{}, attendeeType: Tutor");
            return AttendeeType.TUTOR;
        }

        if (matchUser(space.getManagers(), requester)) {
            log.debug("Live:{}, Requester:{}, attendeeType: Manager");
            return AttendeeType.MANAGER;
        }

        if (matchUser(space.getTutees(), requester)) {
            log.debug("Live:{}, Requester:{}, attendeeType: Tutee");
            return AttendeeType.TUTEE;
        }

        log.debug("Live:{}, Requester:{}, attendeeType: null");
        throw new LiveException(UN_ACCEPTABLE_USER);
    }

    private boolean matchUser(List<User> users, User requester) {
        return users.stream()
                .filter(user -> user.equals(requester))
                .findFirst()
                .isPresent();
    }
}
