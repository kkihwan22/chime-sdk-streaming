package me.taling.live.attendee.applications;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;

public interface AttendChimeComponent {
    Chime execute(JoinedChimeParameter parameter);

    @Getter @ToString
    class JoinedChimeParameter {
        private Live live;
        private User requester;

        public JoinedChimeParameter(Live live, User requester) {
            this.live = live;
            this.requester = requester;
        }
    }
}
