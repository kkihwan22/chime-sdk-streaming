package me.taling.live.core.usecase.action;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.domain.Actions;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.application.space.ActionMessage;
import me.taling.live.core.application.space.SpaceBody;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.BadParameterException;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.FORBIDDEN_DEVICE_LOCKED;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NO_PERMISSION;
import static me.taling.live.global.vo.DeviceStatus.*;

@RequiredArgsConstructor
@Component
public class ActionComponentImpl implements ActionComponent {
    private final Logger log = LoggerFactory.getLogger(ActionComponentImpl.class);
    private final SpaceComponent spaceComponent;
    private final AttendeeDataprovider attendeeDataprovider;
    private final LiveRepositoryComponent liveRepositoryComponent;

    private final static String MESSAGE_TYPE = "attendee_action";
    private final static String[] tutorActions = {"mute", "video_lock", "mic_lock", "video_unlock", "mic_unlock", "screen_share_lock", "screen_share_unlock"};
    private final static String[] attendeeActions = {"video_on", "video_off", "mic_on", "mic_off", "hands_up", "hands_down"};

    // TODO: refactoring - 단일 기능으로...
    @Override
    public void execute(Parameter parameter) {
        log.debug("parameter:{}", parameter);
        Live live = liveRepositoryComponent.findLive(parameter.getLiveId());
        User requester = ThreadLocalContextProvider.get();
        String actionType = parameter.getActionType();

        boolean managed = this.hasPermission(live, requester);

        for (String action : tutorActions) {
            if (action.equals(actionType)) {
                if (!managed) {
                    throw new LiveException(NO_PERMISSION);
                }
                spaceComponent.publish(new SpaceBody(MESSAGE_TYPE, live.getChannelId(), new ActionMessage(actionType, "mute".equals(actionType)
                        ? this.actionMute(live)
                        : this.actionOther(live, parameter.getUserId(), actionType))));



                return;
            }
        }

        for (String action : attendeeActions) {
            log.debug("live:[{}] user:[{}] attendeeType:[{}] actionType [{}]", live.getId(), requester.getId(), managed, actionType);

            if (action.equals(actionType)) {
                Attendee attendee = this.updateMyDevice(live, requester.getId(), actionType);
                spaceComponent.publish(new SpaceBody(MESSAGE_TYPE, live.getChannelId(), new ActionMessage(actionType, Collections.singletonList(new AttendeeResponse(attendee)))));
                return;
            }
        }

        throw new BadParameterException("Not matched action_type");
    }

    private boolean hasPermission(Live live, User requester) {
        Attendee attendee = attendeeDataprovider.findOne(live, requester);
        log.debug("attendee:{}", attendee);

        if (attendee.getAttendeeType() == AttendeeType.TUTOR || attendee.getAttendeeType() == AttendeeType.MANAGER) {
            return true;
        }
        return false;
    }

    private List<AttendeeResponse> actionMute(Live live) {
        log.debug("live:{}, action:mute", live.getId());
        // TODO : 상태.. 조회 방법 @Indexed로 해결안됨
        List<Attendee> attendees = attendeeDataprovider.finds(live)
                .stream()
                .filter(attendee -> attendee.getStatus() == Attendee.AttendeeStatus.ATTENDED)
                .collect(Collectors.toList());;
        if (LiveMethod.INTERACTION == live.getLiveMethod()) {
            this.muteByInteractive(live, attendees);
        } else {
            this.muteByWebinar(live, attendees);
        }
        return attendees.stream().map(attendee -> new AttendeeResponse(attendee)).collect(Collectors.toList());
    }

    private List<AttendeeResponse> actionOther(Live live, Long userId, String actionType) {
        if (userId == null) {
            throw new BadParameterException("Must be not null [userId].");
        }

        User user = User.builder().id(userId).build();
        Attendee attendee = attendeeDataprovider.findOne(live, user);
        log.debug("find attendee:{}", attendee);

        if ("video_lock".equals(actionType)) {
            attendee.setActions(new Actions(LOCK, null, null, null, attendee.getActions()));
        }

        if ("video_unlock".equals(actionType)) {
            attendee.setActions(new Actions(OFF, null, null, null, attendee.getActions()));
        }

        if ("mic_lock".equals(actionType)) {
            attendee.setActions(new Actions(null, LOCK, null, null, attendee.getActions()));
        }

        if ("mic_unlock".equals(actionType)) {
            attendee.setActions(new Actions(null, OFF, null, null, attendee.getActions()));
        }

        if ("hands_up".equals(actionType)) {
            attendee.setActions(new Actions(null, null, ON, null, attendee.getActions()));
        }

        if ("hands_down".equals(actionType)) {
            attendee.setActions(new Actions(null, null, OFF, null, attendee.getActions()));
        }

        if ("screen_share_lock".equals(actionType)) {
            attendee.setActions(new Actions(null, null, null, LOCK, attendee.getActions()));
        }

        if ("screen_share_unlock".equals(actionType)) {
            attendee.setActions(new Actions(null, null, null, OFF, attendee.getActions()));
        }

        attendeeDataprovider.save(attendee);
        return Collections.singletonList(new AttendeeResponse(attendee));
    }


    // todo : 반드시 리팩토링 할 것 !
    private Attendee updateMyDevice(Live live, Long userId, String actionType) {

        User user = User.builder()
                .id(userId)
                .build();
        Attendee attendee = attendeeDataprovider.findOne(live, user);
        log.debug("attendee:{}", attendee);


        if ("video_on".equals(actionType)) {
            if (LOCK == attendee.getVideoStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(ON, null, null, null, attendee.getActions()));
        }

        if ("video_off".equals(actionType)) {
            if (LOCK == attendee.getVideoStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(OFF, null, null, null, attendee.getActions()));
        }

        if ("mic_on".equals(actionType)) {
            if (LOCK == attendee.getMicStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(null, ON, null, null, attendee.getActions()));
        }

        if ("mic_off".equals(actionType)) {
            if (LOCK == attendee.getMicStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(null, OFF, null, null, attendee.getActions()));
        }

        if ("hands_up".equals(actionType)) {
            if (LOCK == attendee.getHandsStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(null, null, ON, null, attendee.getActions()));
        }

        if ("hands_down".equals(actionType)) {
            if (LOCK == attendee.getHandsStatus()) {
                throw new LiveException(FORBIDDEN_DEVICE_LOCKED);
            }
            attendee.setActions(new Actions(null, null, OFF, null, attendee.getActions()));
        }

        attendeeDataprovider.save(attendee);
        return attendee;
    }

    private void muteByWebinar(Live live, List<Attendee> attendees) {
        for (Attendee attendee : attendees) {
            if (attendee.getUserId().equals(live.getTutor().getId())) {
                continue;
            }
            attendee.setActions(new Actions(null, LOCK, null, null, attendee.getActions()));
            attendeeDataprovider.save(attendee);
        }
    }

    private void muteByInteractive(Live live, List<Attendee> attendees) {
        for (Attendee attendee : attendees) {
            Long me = ThreadLocalContextProvider.get().getId();
            if (me.equals(attendee.getUserId())) {
                continue;
            }
            attendee.setActions(new Actions(null, OFF, null, null, attendee.getActions()));
            attendeeDataprovider.save(attendee);
        }
    }
}