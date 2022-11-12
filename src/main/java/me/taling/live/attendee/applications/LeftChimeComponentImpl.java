package me.taling.live.attendee.applications;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.DeleteAttendeeRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LeftChimeComponentImpl implements LeftChimeComponent {
    private final Logger log = LoggerFactory.getLogger(LeftChimeComponentImpl.class);
    private final AmazonChime amazonChime;

    @Override
    public void execute(LeftChimeParameter parameter) {
        // TODO: Exception 처리 코드 추가....
        amazonChime.deleteAttendee(new DeleteAttendeeRequest()
                .withAttendeeId(parameter.getAttendee().getAttendeeId())
                .withMeetingId(parameter.getAttendee().getLive().getMeetingId()));
    }
}
