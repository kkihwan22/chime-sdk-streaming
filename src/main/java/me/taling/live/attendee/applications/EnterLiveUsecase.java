package me.taling.live.attendee.applications;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.DeviceStatus;

public interface EnterLiveUsecase {

    EnterLiveResult execute(EnterLiveParameter parameter);

    @Getter @ToString
    class EnterLiveParameter {
        private Live live;
        private User requester;

        private DeviceStatus videoStatus;
        private DeviceStatus micStatus;

        public EnterLiveParameter(Live live, User requester, DeviceStatus videoStatus, DeviceStatus micStatus) {
            this.live = live;
            this.requester = requester;
            this.videoStatus = videoStatus;
            this.micStatus = micStatus;
        }
    }

    @Getter @ToString
    class EnterLiveResult {
        private Attendee attendee;
        private Chime chime;

        public EnterLiveResult(Attendee attendee, Chime chime) {
            this.attendee = attendee;
            this.chime = chime;
        }
    }
}
