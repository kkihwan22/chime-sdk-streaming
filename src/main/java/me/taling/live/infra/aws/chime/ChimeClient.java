package me.taling.live.infra.aws.chime;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.*;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChimeClient {
    private final Logger log = LoggerFactory.getLogger(ChimeClient.class);
    private final AmazonChime amazonChime;

    public Chime attendBot(Live live) {
        String meetingId = live.getMeetingId();
        GetMeetingResult meeting = amazonChime.getMeeting(new GetMeetingRequest().withMeetingId(meetingId));
        CreateAttendeeResult attendee = amazonChime.createAttendee(new CreateAttendeeRequest().withMeetingId(meetingId).withExternalUserId("Jay"));
        return new Chime(meeting.getMeeting(), attendee.getAttendee());
    }

    public GetMeetingResult findMeeting(ChimeClientParameter parameter) {
        return amazonChime.getMeeting(parameter.getMeetingRequest());
    }
}