package me.taling.live.attendee.domain;

import lombok.*;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.ConnectDevice;
import me.taling.live.global.vo.DeviceStatus;

import java.time.LocalDateTime;

import static me.taling.live.attendee.domain.Attendee.AttendeeStatus.*;
import static me.taling.live.attendee.domain.AttendeeType.*;
import static me.taling.live.global.vo.DeviceStatus.*;
import static me.taling.live.global.vo.LiveMethod.WEBINAR;

@AllArgsConstructor
@Builder
@ToString(exclude = "live")
@EqualsAndHashCode(of = "userId")
@Getter
public class Attendee {
    public enum AttendeeStatus {
        NONE, WAITINGROOM, ATTENDED, EXIT
    }

    private Long userId;
    private Live live;
    private AttendeeType attendeeType;
    private String nickname;
    private String imageUrl;
    private String thumbnailUrl;
    private String attendeeId;
    private Boolean isUsingPushNotification;
    private ConnectDevice connectDevice;

    private AttendeeStatus status;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    private LocalDateTime attendedWaitingroomDatetime;
    private LocalDateTime attendedDatetime;
    private LocalDateTime exitLiveDatetime;

    private Actions actions;

    private DeviceStatus videoStatus;
    private DeviceStatus micStatus;
    private DeviceStatus handsStatus;
    private DeviceStatus screenStatus;

    public Attendee(Live live, User requester, AttendeeType attendeeType, AttendeeStatus status, String attendeeId) {
        // TODO : User로 변경
        this.userId = requester.getId();
        this.nickname = requester.getNickname();
        this.imageUrl = requester.getImageUrl();
        this.thumbnailUrl = requester.getThumbnailUrl();
        this.isUsingPushNotification = requester.getIsUsingPushNotification();
        this.connectDevice = requester.getConnectDevice();
        this.attendeeId = attendeeId;

        // TODO : 하위객체에서 정의
        this.attendeeType = attendeeType;
        this.live = live;

        LocalDateTime now = LocalDateTime.now();
        this.createdDatetime = now;
        this.updatedDatetime = now;
        this.attendedWaitingroomDatetime = now;
        this.status = status;
    }

    public void initDeviceStatus() {
        switch (this.attendeeType) {
            case TUTOR:
                this.setActions(new Actions(ON, ON, OFF, OFF));
                break;
            case MANAGER:
                this.setActions(live.getLiveMethod() == WEBINAR
                        ? new Actions(LOCK, LOCK, OFF, LOCK)
                        : new Actions(ON, ON, OFF, OFF));
                break;
            case TUTEE:
                this.setActions(live.getLiveMethod() == WEBINAR
                        ? new Actions(LOCK, LOCK, OFF, LOCK)
                        : new Actions(ON, ON, OFF, LOCK));
                break;
            default:
        }
    }

    public void attend(Actions actions) {
        LocalDateTime now = LocalDateTime.now();
        this.updatedDatetime = now;
        this.attendedDatetime = now;
        this.status = ATTENDED;

        switch (this.attendeeType) {
            case TUTOR:
                this.setActions(actions);
                break;
            case MANAGER:
            case TUTEE:
                this.setActions(live.getLiveMethod() == WEBINAR
                        ? new Actions(LOCK, LOCK, OFF, LOCK)
                        : actions);
                break;
            default:
        }
    }

    public void reentrant() {
        LocalDateTime now = LocalDateTime.now();
        this.attendedWaitingroomDatetime = now;
        this.updatedDatetime = now;
        this.status =  WAITINGROOM;

        if (this.live.getLiveMethod() == WEBINAR) {
            // TODO: 추상화 모델에 따른 다른 구현
            if (this.attendeeType != TUTOR) {
                this.setActions(new Actions(LOCK, LOCK, OFF, null, this.getActions()));
            }
        }
    }

    public void exit() {
        LocalDateTime now = LocalDateTime.now();
        this.updatedDatetime = now;
        this.exitLiveDatetime = now;
        this.status = EXIT;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
        this.micStatus = actions.getMicStatus();
        this.videoStatus = actions.getVideoStatus();
        this.handsStatus = actions.getHandsStatus();
        this.screenStatus = actions.getScreenStatus();
    }

    public void changedRoleForManager(Live live) {
        this.attendeeType = MANAGER;
        this.setActions(WEBINAR == live.getLiveMethod()
                ? new Actions(LOCK, LOCK, null, OFF, this.actions)
                : new Actions(null, null, null, OFF, this.actions));
    }

    public void changedRoleForTutee(Live live) {
        this.attendeeType = TUTEE;
        this.setActions(this.actions = WEBINAR == live.getLiveMethod()
                ? new Actions(LOCK, LOCK, OFF, LOCK, this.actions)
                : new Actions(OFF, OFF, OFF, LOCK, this.actions));
    }
}