package me.taling.live.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.AttendeeType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
public class Space {
    private String channelId;
    private String referenceId;
    private String referenceType;
    private String title;
    private User tutor;
    private List<User> managers;
    private List<User> tutees;
    private Boolean isPublic;

    public Space(String channelId) {
        this.channelId = channelId;
    }

    public Space(String channelId, String referenceId, String referenceType, String title, User tutor, List<User> managers, List<User> tutees, Boolean isPublic) {
        this.channelId = channelId;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.title = title;
        this.tutor = tutor;
        this.managers = managers;
        this.tutees = tutees;
        this.isPublic = isPublic;
    }

    public AttendeeType decideAttendeeType(User user) {

        if (this.tutor.isMatchedId(user.getId())) {
            return AttendeeType.TUTOR;
        }

        if (this.isMatched(managers, user.getId())) {
            return AttendeeType.MANAGER;
        }

        if (this.isMatched(tutees, user.getId())) {
            return AttendeeType.TUTEE;
        }

        return AttendeeType.GUEST;
    }

    public void updateTutee(User user) {
        if (this.tutees == null) {
            tutees = new ArrayList<>();
        }
        tutees.add(user);
    }

    public void updateManagerRole(User user) {
        if (this.managers == null) {
            this.managers = new ArrayList<>();
        }
        this.managers.add(user);

        if (!CollectionUtils.isEmpty(this.tutees)) {
            this.tutees.remove(user);
        }
    }

    public void updateTuteeRole(User user) {
        if (this.tutees == null) {
            this.tutees = new ArrayList<>();
        }
        this.tutees.add(user);

        if (!CollectionUtils.isEmpty(this.managers)) {
            this.managers.remove(user);
        }
    }

    private boolean isMatched(List<User> users, Long userId) {
        boolean matched = false;
        for (User user : users) {
            if (user.getId().equals(userId)) {
                matched = true;
                break;
            }
        }
        return matched;
    }
}