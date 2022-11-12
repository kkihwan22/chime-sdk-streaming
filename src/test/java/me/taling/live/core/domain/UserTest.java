package me.taling.live.core.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testEquals() {

        User user1 = User.builder()
                .id(1000L)
                .nickname("테스트1")
                .build();

        User user3 = User.builder()
                .id(1000L)
                .nickname("테스트3")
                .build();

        boolean actual = user1.equals(user3);
        Assertions.assertEquals(true, actual);
    }

    @Test
    void testHashCode() {
    }
}