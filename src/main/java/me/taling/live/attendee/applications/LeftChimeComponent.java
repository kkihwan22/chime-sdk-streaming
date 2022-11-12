package me.taling.live.attendee.applications;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;

public interface LeftChimeComponent {
    void execute(LeftChimeParameter parameter);

    @Getter @ToString
    class LeftChimeParameter {
        private Attendee attendee;

        public LeftChimeParameter(Attendee attendee) {
            this.attendee = attendee;
        }
    }
}
