package me.taling.live.attendee.applications.dto;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.core.domain.Chime;

@Getter @ToString
public class AttendeeResult {
    private Attendee attendee;
    private Chime chime;

    public AttendeeResult(Attendee attendee, Chime chime) {
        this.attendee =attendee;
        this.chime = chime;
    }

}
