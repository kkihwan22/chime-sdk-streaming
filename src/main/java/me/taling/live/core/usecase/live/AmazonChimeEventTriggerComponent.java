package me.taling.live.core.usecase.live;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public interface AmazonChimeEventTriggerComponent {

    void execute(AmazonChimeEventParameter parameter);

    @Getter
    @ToString
    @EqualsAndHashCode
    class AmazonChimeEventParameter {
        private String eventName;
        private String meetingId;
        private String attendeeId;

        public AmazonChimeEventParameter(String type, String meetingId, String attendeeId) {
            this.eventName = type;
            this.meetingId = meetingId;
            this.attendeeId = attendeeId;
        }
    }
}
