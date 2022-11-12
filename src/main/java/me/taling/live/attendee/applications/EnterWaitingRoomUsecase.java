package me.taling.live.attendee.applications;

import me.taling.live.attendee.applications.dto.AttendeeParameter;
import me.taling.live.attendee.applications.dto.AttendeeResult;

public interface EnterWaitingRoomUsecase {

    AttendeeResult execute(AttendeeParameter parameter);
}
