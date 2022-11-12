package me.taling.live.core.usecase.live;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.domain.Actions;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Live;
import me.taling.live.core.usecase.live.command.LiveExpireComponent;
import me.taling.live.global.exceptions.BadParameterException;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static me.taling.live.attendee.domain.Attendee.AttendeeStatus.EXIT;
import static me.taling.live.global.vo.DeviceStatus.OFF;
import static me.taling.live.global.vo.DeviceStatus.ON;

@RequiredArgsConstructor
@Component
public class AmazonChimeEventTriggerComponentImpl implements AmazonChimeEventTriggerComponent {
    private final Logger log = LoggerFactory.getLogger(AmazonChimeEventTriggerComponentImpl.class);
    private final LiveRepositoryComponent liveRepositoryComponent;
    private final AttendeeDataprovider attendeeDataprovider;
    private final SpaceComponent spaceComponent;
    private final LiveExpireComponent liveExpireComponent;

    @Override
    public void execute(AmazonChimeEventParameter parameter) {
        log.debug("AmazonChimeEventParameter :{}", parameter);

        Live live = liveRepositoryComponent.findLiveByMeetingId(parameter.getMeetingId());
        log.debug("live:{}", live);
        Type from = Type.from(parameter.getEventName());
        log.debug("event type:{}", from);
        Attendee attendee;
        switch (from) {
            case CONTENT_JOINED:
                attendee = attendeeDataprovider.findByLiveIdAndAttendeeId(live, parameter.getAttendeeId());
                attendee.setActions(new Actions(null, null, null, ON, attendee.getActions()));
                attendeeDataprovider.save(attendee);
                spaceComponent.publish(new SpaceBody("screen_share_on", live.getChannelId(), new AttendeeResponse(attendee)));
                break;
            case CONTENT_LEFT:
                attendee = attendeeDataprovider.findByLiveIdAndAttendeeId(live, parameter.getAttendeeId());
                attendee.setActions(new Actions(null, null, null, OFF, attendee.getActions()));
                attendeeDataprovider.save(attendee);
                spaceComponent.publish(new SpaceBody("screen_share_off", live.getChannelId(), new AttendeeResponse(attendee)));
                break;
            case ATTENDEE_DROPPED:
                this.executeDroppedAttendee(live, attendeeDataprovider.findByLiveIdAndAttendeeId(live, parameter.getAttendeeId()));
                break;
            case MEETING_ENDED:
                log.debug("MEETING ENDED. Live:{}", live);
                liveExpireComponent.execute(live);
                break;
            default:
                log.debug("do nothing.");
        }
    }

    private void executeDroppedAttendee(Live live, Attendee attendee) {
        if (attendee == null) {
            log.error("Already exit user.");
            throw new LiveException(LiveException.ErrorCode.NOT_FOUND_ATTENDEE);
        }

        if (attendee.getStatus() == EXIT) {
            log.error("Already exit user.");
            throw new LiveException(LiveException.ErrorCode.ALREADY_LEFT_ATTENDEE);
        }

        attendee.exit();
        spaceComponent.publish(new SpaceBody("attendee_leave", live.getChannelId(), Collections.singletonList(new AttendeeResponse(attendee))));
        attendeeDataprovider.save(attendee);
    }

    enum Type {
        MEETING_STARTED("chime:MeetingStarted"),
        MEETING_ENDED("chime:MeetingEnded"),
        ATTENDEE_ADDED("chime:AttendeeAdded"),
        ATTENDEE_JOINED("chime:AttendeeJoined"),
        ATTENDEE_LEFT("chime:AttendeeLeft"),
        ATTENDEE_DROPPED("chime:AttendeeDropped"),
        ATTENDEE_REMOVED("chime:AttendeeDeleted"),
        VIDEO_STARTED("chime:AttendeeVideoStarted"),
        VIDEO_STOPPED("chime:AttendeeVideoStopped"),
        CONTENT_JOINED("chime:AttendeeContentJoined"),
        CONTENT_LEFT("chime:AttendeeContentLeft"),
        CONTENT_DROPPED("chime:AttendeeContentDropped"),
        CONTENT_VIDEO_STARTED("chime:AttendeeContentVideoStarted"),
        CONTENT_VIDEO_STOPPED("chime:AttendeeContentVideoStopped"),
        ;

        @Getter
        private String eventName;

        Type(String eventName) {
            this.eventName = eventName;
        }

        private static final Map<String, Type> map =
                Stream.of(Type.values()).collect(Collectors.toMap(Type::getEventName, Function.identity()));

        public static Type from(String eventName) {
            return ofNullable(map.get(eventName))
                    .orElseThrow(() -> new BadParameterException("Not matched event name."));
        }
    }
}
