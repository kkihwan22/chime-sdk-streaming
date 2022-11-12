package me.taling.live.infra.aws.chime;

import com.amazonaws.services.chime.model.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
public class ChimeClientParameter {
    private final static int DEFAULT_MAX_RESULT = 99;
    private String liveId;
    private String meetingId;
    private String externalMeetingId;
    private String attendeeId;
    private String externalUserId;
    private Integer maxResult;
    private String nextToken;

    public int getMaxResult() {
        return this.maxResult == null || this.maxResult == 0
                ? DEFAULT_MAX_RESULT
                : this.maxResult;
    }

    public CreateMeetingRequest createMeetingRequest() {
        return new CreateMeetingRequest()
                .withExternalMeetingId(externalMeetingId);
    }

    public CreateAttendeeRequest createAttendeeRequest() {
        return new CreateAttendeeRequest()
                .withMeetingId(this.meetingId)
                .withExternalUserId(this.externalUserId);
    }

    public GetMeetingRequest getMeetingRequest() {
        return new GetMeetingRequest()
                .withMeetingId(this.meetingId);
    }

    public GetAttendeeRequest getAttendeeRequest() {
        return new GetAttendeeRequest()
                .withAttendeeId(this.attendeeId)
                .withMeetingId(this.meetingId);
    }

    public ListAttendeesRequest getListAttendeeRequest() {
        return new ListAttendeesRequest()
                .withMeetingId(this.meetingId)
                .withMaxResults(this.getMaxResult())
                .withNextToken(this.nextToken);
    }
}
