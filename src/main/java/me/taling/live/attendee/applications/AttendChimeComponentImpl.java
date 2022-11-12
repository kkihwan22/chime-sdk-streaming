package me.taling.live.attendee.applications;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.*;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_CHIME_MEETING;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.UNKNOWN_ERROR_CHIME;

@RequiredArgsConstructor
@Component
public class AttendChimeComponentImpl implements AttendChimeComponent {
    private final Logger log = LoggerFactory.getLogger(AttendChimeComponentImpl.class);
    private final AmazonChime amazonChime;

    @Override
    public Chime execute(JoinedChimeParameter parameter) {
        Live live = parameter.getLive();
        String externalNickname = this.adjustNicknameLength(String.valueOf(parameter.getRequester().getId()));

        try {
            GetMeetingResult meeting = amazonChime.getMeeting(new GetMeetingRequest()
                    .withMeetingId(live.getMeetingId())
            );

            CreateAttendeeResult attendee = amazonChime.createAttendee(new CreateAttendeeRequest()
                    .withMeetingId(live.getMeetingId())
                    .withExternalUserId(externalNickname)
            );

            return new Chime(meeting.getMeeting(), attendee.getAttendee());

        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                log.error("live:{}, requester:{}, meetingId:{} [NOT FOUNDED CHIME MEETING]", live.getId(), parameter.getRequester().getId(), live.getMeetingId());
                throw new LiveException(NOT_FOUND_CHIME_MEETING);
            }

            log.error("live:{}, requester:{}, meetingId:{}: [WHEN ATTEND CHIME. UNKNOWN ERROR], err:{}", live.getId(), parameter.getRequester().getId(), live.getMeetingId(), e);
            throw new LiveException(UNKNOWN_ERROR_CHIME);
        }
    }

    private String adjustNicknameLength(String nickname) {
        return nickname.length() > 1 ? nickname : "0".concat(nickname);
    }
}
