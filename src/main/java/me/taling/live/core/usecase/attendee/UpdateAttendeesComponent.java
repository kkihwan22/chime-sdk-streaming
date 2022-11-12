package me.taling.live.core.usecase.attendee;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;

import java.util.List;

public interface UpdateAttendeesComponent {

    UpdateAttendeesResult execute(UpdateAttendeesParameter parameter);

    @ToString
    @EqualsAndHashCode
    @Getter
    class UpdateAttendeesParameter {
        private Live live;
        private List<User> addUsers;
        private List<User> removeUsers;

        public UpdateAttendeesParameter(Live live, List<User> addUsers, List<User> removeUsers) {
            this.live = live;
            this.addUsers = addUsers;
            this.removeUsers = removeUsers;
        }
    }


    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class UpdateAttendeesResult {
        private String liveId;
    }
}
