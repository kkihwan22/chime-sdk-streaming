package me.taling.live.global.vo;

import lombok.*;
import me.taling.live.core.usecase.tutor.UpdateDeviceParameter;
import me.taling.live.global.exceptions.BadParameterException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
public class DeviceStatusSet {
    private DeviceStatus video;
    private DeviceStatus audio;
    private DeviceStatus share;

    public void update(UpdateDeviceParameter parameter) {
        this.update(parameter.getDeviceType(), parameter.getDeviceStatus());
    }

    public void update(DeviceType type, DeviceStatus status) {
        // todo : 코드 리팩토링 필요! (enum으로?!)
        switch (type) {
            case VIDEO:
                this.video = status;
                break;
            case AUDIO:
                this.audio = status;
                break;
            case SHARE:
                this.share = status;
                break;
            default:
                throw new BadParameterException("Not matched device type : [" + type.name() + "]");
        }
    }
}