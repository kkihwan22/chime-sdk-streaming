package me.taling.live.core.usecase.live;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AmazonChimeEventTriggerComponentImplTest {


    @Test
    public void 테스트_type () {
        AmazonChimeEventTriggerComponentImpl.Type from = AmazonChimeEventTriggerComponentImpl.Type.from("chime:MeetingEnded");
        assertNotNull(from);
    }

}