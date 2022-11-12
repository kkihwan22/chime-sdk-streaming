package me.taling.live.attendee.applications;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.applications.AttendChimeComponent.JoinedChimeParameter;
import me.taling.live.attendee.applications.dto.AttendeeParameter;
import me.taling.live.attendee.applications.dto.AttendeeResult;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static me.taling.live.attendee.domain.Attendee.AttendeeStatus;

@RequiredArgsConstructor
@Service
public class EnterWaitingRoomService implements EnterWaitingRoomUsecase {
    private final Logger log = LoggerFactory.getLogger(EnterWaitingRoomService.class);
    private final BeforeEnterLiveSpaceStrategyFactory factory;
    private final AttendChimeComponent attendChimeComponent;
    private final AttendeeDataprovider attendeeDataprovider;

    @Override
    public AttendeeResult execute(AttendeeParameter parameter) {
        Live live = parameter.getLive();
        User requester = parameter.getRequester();
        log.debug("Enter Watingroom. Live:{},Requester:{}", live.getId(), requester.getId());

        // TODO: Attendee 객체 추상화를 통해 TUTOR, TUTEE, MANAGER 같은 객체 생성.
        Attendee attendee = attendeeDataprovider.findOne(live, requester);
        if (attendee == null) {
            AttendeeType role = factory.getStrategyBean(live).execute(live, requester);
            // TODO: Chime 저장하자.
            Chime chimeResult = attendChimeComponent.execute(new JoinedChimeParameter(live, requester));
            attendee = new Attendee(
                    live,
                    requester,
                    role,
                    AttendeeStatus.WAITINGROOM,
                    chimeResult.getAttendee().getAttendeeId()
            );

            attendee.initDeviceStatus();
            attendeeDataprovider.save(attendee);
            return new AttendeeResult(attendee, chimeResult);
        }

        attendee.reentrant();
        attendeeDataprovider.save(attendee);
        Chime chimeResult = attendChimeComponent.execute(new JoinedChimeParameter(live, requester));
        return new AttendeeResult(attendee, chimeResult);
    }
}
