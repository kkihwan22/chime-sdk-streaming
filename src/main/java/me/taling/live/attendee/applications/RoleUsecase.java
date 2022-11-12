package me.taling.live.attendee.applications;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.AttendeeType;

public interface RoleUsecase {

    void execute(RoleParameter parameter);

    @Getter @ToString @EqualsAndHashCode
    class RoleParameter {
        private String liveId;
        private Long userId;
        private AttendeeType type;

        public RoleParameter(String liveId, Long userId, AttendeeType type) {
            this.liveId = liveId;
            this.userId = userId;
            this.type = type;
        }
    }
}
