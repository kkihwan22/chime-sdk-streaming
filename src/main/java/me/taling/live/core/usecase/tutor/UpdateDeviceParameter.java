package me.taling.live.core.usecase.tutor;

import lombok.*;
import me.taling.live.global.vo.DeviceStatus;
import me.taling.live.global.vo.DeviceType;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class UpdateDeviceParameter {
    private Long userId;
    private DeviceType deviceType;
    private DeviceStatus deviceStatus;
}
