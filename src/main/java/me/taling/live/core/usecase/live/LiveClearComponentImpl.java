package me.taling.live.core.usecase.live;

import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.model.ListAttendeesRequest;
import com.amazonaws.services.chime.model.ListAttendeesResult;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Live;
import me.taling.live.core.usecase.live.command.LiveExpireComponent;
import me.taling.live.infra.repository.LiveRepositoryComponentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LiveClearComponentImpl implements LiveClearComponent {
    private final Logger log = LoggerFactory.getLogger(LiveClearComponentImpl.class);
    private final LiveRepositoryComponentImpl liveRepositoryComponent;
    private final AmazonChime amazonChime;
    private final LiveExpireComponent liveExpireComponent;

    @Override
    public void execute() {
        log.debug("start live clear");
        List<Live> lives = liveRepositoryComponent.findLives();
        log.debug("lives: {}", lives);

        for (Live live : lives) {
            LocalDateTime now = LocalDateTime.now();
            if (live.getEndDatetime() == null) {
                // TODO: 잠시 데이터가 안맞아서 포함한 로직입니다.
                continue;
            }

            if (now.isBefore(live.getEndDatetime().plusMinutes(30))) {
                log.debug("retry live clear. liveId:[{}], start:[{}], end:[{}], now:[{}]", live.getId(), live.getStartDatetime(), live.getEndDatetime(), now);
                continue;
            }


            if (isClear(live, now)) {
                log.debug("start live clear. liveId:[{}], start:[{}], end:[{}], now:[{}]", live.getId(), live.getStartDatetime(), live.getEndDatetime(), now);
                liveExpireComponent.execute(live);
            }
        }
    }

    private boolean isClear(Live live, LocalDateTime now) {
        if (now.isAfter(live.getEndDatetime().plusHours(2))) {
            return true;
        }

        if (getAttendeeCount(live.getMeetingId()) > 0) {
            return false;
        }

        return true;
    }

    private int getAttendeeCount(String meetingId) {

        try {
            ListAttendeesResult results = amazonChime.listAttendees(new ListAttendeesRequest().withMeetingId(meetingId));
            log.debug("results(attendee chime): {}", results);

            int size = results.getAttendees().size();
            if (size == 1) {
                return results.getAttendees().get(0).getExternalUserId().equals("Jay")
                        ? 0
                        : 1;
            }

            return size;
        } catch (Exception e) {
            log.error("error get chime. reason: {}", e);
            return 0;
        }
    }
}
