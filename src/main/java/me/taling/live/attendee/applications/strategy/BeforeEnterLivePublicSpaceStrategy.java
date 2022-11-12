package me.taling.live.attendee.applications.strategy;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.applications.BeforeEnterLiveSpaceStrategy;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceParameter;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BeforeEnterLivePublicSpaceStrategy implements BeforeEnterLiveSpaceStrategy {
    private final Logger log = LoggerFactory.getLogger(BeforeEnterLivePublicSpaceStrategy.class);
    private final SpaceComponent spaceComponent;

    // TODO: Space profile 조회 (단건 조회, 단건 수정으로 로직 변경이 필요)
    @Override
    public AttendeeType execute(Live live, User requester) {
        Space space = spaceComponent.findOne(live.getChannelId());
        if (space.getTutor().equals(requester)) {
            log.debug("Live:{}, Requester:{}, attendeeType: Tutor");
            return AttendeeType.TUTOR;
        }

        space.getTutees().add(requester);
        // TODO : space startAt, expireAt 정보 추가
        spaceComponent.put(live.getChannelId(), new SpaceParameter(space.getReferenceId()
                , space.getReferenceType()
                , space.getTitle()
                , live.getTutor()
                , space.getManagers()
                , space.getTutees()
                , space.getIsPublic()));
        return AttendeeType.TUTEE;
    }
}
