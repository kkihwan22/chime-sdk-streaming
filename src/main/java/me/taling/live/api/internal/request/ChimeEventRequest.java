package me.taling.live.api.internal.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class ChimeEventRequest {
    private String version;
    private String eventType;
    private String meetingId;
    private String attendeeId;
    private String externalUserId;
    private Long timestamp;
}
