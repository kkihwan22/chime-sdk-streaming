package me.taling.live.core.usecase.tutor;

import lombok.*;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.DeviceStatus;
import me.taling.live.global.vo.DeviceType;

public interface TutorComponent {
    void startLive(Live live, User requester);
    void closeLive(Parameter parameter);


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    @EqualsAndHashCode
    @Getter
    class Parameter {
        private String liveId;
        private UpdateDeviceParameter updateDeviceParameter;

        @Setter
        private Live live;
        @Setter
        private User requester;

        public Parameter(String liveId) {
            this.liveId = liveId;
        }

        public Parameter(String liveId, Long userId, String type, String status) {
            this.liveId = liveId;
            this.updateDeviceParameter = UpdateDeviceParameter.builder()
                    .userId(userId)
                    .deviceType(DeviceType.valueOf(type))
                    .deviceStatus(DeviceStatus.valueOf(status))
                    .build();
        }
    }
}
