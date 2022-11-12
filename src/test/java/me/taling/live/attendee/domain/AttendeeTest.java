package me.taling.live.attendee.domain;

import me.taling.live.core.domain.Live;
import me.taling.live.global.vo.LiveMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static me.taling.live.global.vo.DeviceStatus.OFF;

class AttendeeTest {


    @Test
    void test_attend() {
        Attendee attendee = Attendee.builder()
                .userId(1000L)
                .attendeeType(AttendeeType.TUTEE)
                .live(new Live("test_live")
                        .withLiveMethod(LiveMethod.INTERACTION))
                .build();

        attendee.initDeviceStatus();
        Actions actions = new Actions(OFF, OFF, OFF, null, attendee.getActions());
        attendee.attend(actions);

        Assertions.assertEquals(OFF, attendee.getVideoStatus());
        Assertions.assertEquals(OFF, attendee.getMicStatus());
    }
}