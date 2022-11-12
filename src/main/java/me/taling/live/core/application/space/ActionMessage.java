package me.taling.live.core.application.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
public class ActionMessage {
    private String type;
    private List<AttendeeResponse> attendees;

    public ActionMessage(String type, List<AttendeeResponse> attendees) {
        this.type = type;
        this.attendees = attendees;
    }

}
