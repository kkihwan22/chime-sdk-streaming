package me.taling.live.core.usecase.query;

import lombok.RequiredArgsConstructor;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Lives;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class QueryComponentImpl implements QueryComponent {
    private final Logger log = LoggerFactory.getLogger(QueryComponentImpl.class);
    private final LiveRepositoryComponent liveRepositoryComponent;
    private final AttendeeDataprovider attendeeDataprovider;

    @Override
    public List<Lives> lives() {
        List<Lives> lives = liveRepositoryComponent.findLives().stream()
                .map(entity -> new Lives(entity))
                .collect(Collectors.toList());
        log.debug("lives:{}", lives);
        return lives;
    }

    @Override
    public Live live(String liveId) {
        return liveRepositoryComponent.findLive(liveId);
    }


    @Override
    public List<Attendee> attendees(Live live, Attendee.AttendeeStatus status) {
        return attendeeDataprovider.finds(live)
                .stream()
                .filter(attendee -> attendee.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentsResult users(String liveId) {
        Live live = liveRepositoryComponent.findLive(liveId);
        log.debug("live:{}", live);
        return new EnrollmentsResult(live);
    }
}