package me.taling.live.core.usecase.query;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.Attendee.AttendeeStatus;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Lives;
import me.taling.live.core.domain.User;

import java.util.List;

public interface QueryComponent {

    List<Lives> lives();

    Live live(String liveId);

    List<Attendee> attendees(Live live, AttendeeStatus status);

    EnrollmentsResult users(String liveId);

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class EnrollmentsResult {
        private User tutor;
        private List<User> tutees;

        public EnrollmentsResult(Live live) {
            this.tutor = live.getTutor();
            this.tutees = live.getTutees();
        }
    }
}
