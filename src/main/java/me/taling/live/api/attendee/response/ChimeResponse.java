package me.taling.live.api.attendee.response;

import com.amazonaws.services.chime.model.Attendee;
import com.amazonaws.services.chime.model.Meeting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.taling.live.core.domain.Chime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class ChimeResponse {
    @Schema(description = "aws-chime의 meeting정보")
    private Meeting meeting;

    @Schema(description = "aws-chime의 attendee정보")
    private Attendee attendee;

    public ChimeResponse(Chime chime) {
        this.meeting = chime.getMeeting();
        this.attendee = chime.getAttendee();
    }

}
