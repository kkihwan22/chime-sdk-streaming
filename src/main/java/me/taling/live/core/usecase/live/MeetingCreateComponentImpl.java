package me.taling.live.core.usecase.live;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.sequence.SequenceGenerator;
import me.taling.live.core.domain.Live;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.BadParameterException;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;
import me.taling.live.global.vo.StreamMethod;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MeetingCreateComponentImpl implements CreateComponent {
    private final Logger log = LoggerFactory.getLogger(MeetingCreateComponentImpl.class);
    private final SequenceGenerator sequenceGenerator;
    private final LiveRepositoryComponent liveRepositoryComponent;

    public CreateResult execute(CreateParameter parameter) {
        log.debug("parameter:{}", parameter);

        if (parameter.getStartDatetime().isBefore(LocalDateTime.now().plusMinutes(10L))) {
            log.error("The reservation time must be after the current time. request startDatetime:{}", parameter.getStartDatetime());
            throw new BadParameterException("The reservation time must be after the current time(+10min).");
        }

        Live live = new Live(sequenceGenerator.generate())
                .withType(LiveType.INSTANCE)
                .withLiveMethod(Optional.ofNullable(parameter.getLiveMethod()).orElse(LiveMethod.INTERACTION))
                .withStreamMethod(StreamMethod.WEBRTC)
                .withTitle(parameter.getTitle())
                .withRecordingCondition(parameter.getRecCondition())
                .withTutor(ThreadLocalContextProvider.get())
                .withTutees(Collections.emptyList())
                .withManagers(Collections.emptyList())
                .withStartDatetime(Optional.ofNullable(parameter.getStartDatetime())
                        .orElseThrow(() -> new BadParameterException("[startDatetime] should not be NULL")))
                .withEndDatetime(parameter.getStartDatetime().plusHours(1))
                .withExpireDatetime(parameter.getStartDatetime().plusHours(2));

        log.debug("live:{}", live);
        liveRepositoryComponent.reservedLive(live);
        return new CreateResult(live);
    }
}
