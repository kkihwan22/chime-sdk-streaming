package me.taling.live.attendee.applications;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.applications.LeftChimeComponent.LeftChimeParameter;
import me.taling.live.attendee.applications.dto.AttendeeParameter;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static me.taling.live.attendee.domain.Attendee.AttendeeStatus.EXIT;

@RequiredArgsConstructor
@Service
public class ExitLiveService implements ExitLiveUsecase {
    private final Logger log = LoggerFactory.getLogger(ExitLiveService.class);
    private final AttendeeDataprovider attendeeDataprovider;
    private final LeftChimeComponent leftChimeComponent;
    private final SpaceComponent spaceComponent;

    private final static String TYPE = "attendee_leave";

    @Override
    public void execute(AttendeeParameter parameter) {
        Live live = parameter.getLive();
        User requester = parameter.getRequester();
        log.debug("Exit live. Live:{},Requester:{}", live.getId(), requester.getId());

        Attendee attendee = attendeeDataprovider.findOne(live, requester);

        if (attendee == null) {
            log.error("[NotFound] live:{}, user:{}", live.getId(), requester.getId());
            throw new LiveException(LiveException.ErrorCode.NOT_FOUND_ATTENDEE);
        }

        if (attendee.getStatus() == EXIT) {
            log.error("[Already left attendee] live:{}, user:{}, exited datetime:{}" , live.getId(), requester.getId(), attendee.getExitLiveDatetime());
            throw new LiveException(LiveException.ErrorCode.ALREADY_LEFT_ATTENDEE);
        }

        attendee.exit();
        leftChimeComponent.execute(new LeftChimeParameter(attendee));
        spaceComponent.publish(new SpaceBody(TYPE, live.getChannelId(), Collections.singletonList(new AttendeeResponse(attendee))));
        attendeeDataprovider.save(attendee);
    }

    // TODO: Dataprovider > Adapter > infra (Space는 하나의 infra..)
    // TODO: Component Infra 레이어로 이동....
    // TODO:
}
