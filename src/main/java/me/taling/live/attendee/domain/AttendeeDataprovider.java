package me.taling.live.attendee.domain;

import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;

import java.util.List;

public interface AttendeeDataprovider {

    void save(Attendee attendee);
    Attendee findOne(Live live, User requester);
    List<Attendee> finds(Live live);
    Attendee findByLiveIdAndAttendeeId(Live live, String attendeeId);
}
