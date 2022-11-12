package me.taling.live.attendee.applications.dto;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;

@Getter @ToString
public class AttendeeParameter {

    private Live live;
    private User requester;

    public AttendeeParameter(Live live, User requester) {
        this.live = live;
        this.requester = requester;

    }
}