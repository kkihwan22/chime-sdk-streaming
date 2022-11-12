package me.taling.live.attendee.applications;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.domain.Actions;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.usecase.tutor.TutorComponent;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_ATTENDEE;
import static me.taling.live.global.vo.DeviceStatus.OFF;

@RequiredArgsConstructor
@Service
public class EnterLiveService implements EnterLiveUsecase {
    private final Logger log = LoggerFactory.getLogger(EnterLiveService.class);
    private final AttendeeDataprovider attendeeDataprovider;
    private final SpaceComponent spaceComponent;
    private final AttendChimeComponent attendChimeComponent;

    private final TutorComponent tutorComponent;

    private final static String TYPE = "attendee_join";

    @Override
    public EnterLiveResult execute(EnterLiveParameter parameter) {
        log.debug("Enter Live:{},Requester:{}", parameter.getLive(), parameter.getRequester().getId());

        Attendee attendee = Optional
                .ofNullable(attendeeDataprovider.findOne(parameter.getLive(), parameter.getRequester()))
                .orElseThrow(() -> new LiveException(NOT_FOUND_ATTENDEE));

        // TODO: 한명만 update 하는
        log.debug("parameter video:{}, audio:{}", parameter.getVideoStatus(), parameter.getMicStatus());
        Actions actions = new Actions(
                parameter.getVideoStatus(),
                parameter.getMicStatus(),
                OFF,
                null,
                attendee.getActions());

        attendee.attend(actions);
        spaceComponent.publish(new SpaceBody(TYPE, parameter.getLive().getChannelId(), Collections.singletonList(new AttendeeResponse(attendee))));
        attendeeDataprovider.save(attendee);

        if (AttendeeType.TUTOR == attendee.getAttendeeType()) {
            log.info("Entered tutor ({}). entered time: {} ", attendee.getUserId(), LocalDateTime.now());
            tutorComponent.startLive(parameter.getLive(), parameter.getRequester());
        }

        // TODO: chime 관련 정보도 redis에 저장...
        return new EnterLiveResult(attendee, attendChimeComponent.execute(new AttendChimeComponent.JoinedChimeParameter(parameter.getLive(), parameter.getRequester())));
    }
}