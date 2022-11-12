package me.taling.live.attendee.domain;
//TODO : rename AttendeeType > Role (space 와 용어통일)
/**
 * TUTOR: 강사
 * TUTEE: 수강생
 * MANAGER: 조교
 * SPEAKER: 연사
 */
public enum AttendeeType {
    TUTOR,
    TUTEE,
    BOT,
    MANAGER,
    GUEST,
    ;
}
