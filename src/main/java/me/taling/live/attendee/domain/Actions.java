package me.taling.live.attendee.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.global.vo.DeviceStatus;

import java.util.Optional;

@NoArgsConstructor
@Getter @ToString @EqualsAndHashCode
public class Actions {
    private DeviceStatus videoStatus;
    private DeviceStatus micStatus;
    private DeviceStatus handsStatus;
    private DeviceStatus screenStatus;

    public Actions(
            DeviceStatus videoStatus,
            DeviceStatus micStatus,
            DeviceStatus handsStatus,
            DeviceStatus screenStatus) {

        this.videoStatus = videoStatus;
        this.handsStatus = handsStatus;
        this.micStatus = micStatus;
        this.screenStatus = screenStatus;
    }

    public Actions(DeviceStatus videoStatus,
                   DeviceStatus micStatus,
                   DeviceStatus handsStatus,
                   DeviceStatus screenStatus, Actions currentActions) {

        this(
                Optional.ofNullable(videoStatus)
                        .orElse(currentActions.getVideoStatus()),
                Optional.ofNullable(micStatus)
                        .orElse(currentActions.getMicStatus()),
                Optional.ofNullable(handsStatus)
                        .orElse(currentActions.getHandsStatus()),
                Optional.ofNullable(screenStatus)
                        .orElse(currentActions.getScreenStatus())
        );
    }
}