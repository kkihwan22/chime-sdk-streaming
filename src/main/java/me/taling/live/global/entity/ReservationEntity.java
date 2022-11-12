package me.taling.live.global.entity;

import lombok.*;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.StreamMethod;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Getter
@RedisHash("reservations")
public class ReservationEntity {
    @Id
    private String id;
    @Indexed
    private String startDatetime;

    private String title;
    private User tutor;
    private LiveMethod liveMethod;
    private StreamMethod streamMethod;
    private Boolean recordingCondition;

    public ReservationEntity(Live live) {
        this.id = live.getId();
        this.startDatetime = live.getStartDatetime().format(Live.getLiveDatetimeFormatter());
        this.title = live.getTitle();
        this.tutor = live.getTutor();
        this.liveMethod = live.getLiveMethod();
        this.streamMethod = live.getStreamMethod();
        this.recordingCondition = live.getRecordingCondition();
    }

    public Live toLive() {
        return Live.builder()
                .id(this.id)
                .liveMethod(liveMethod)
                .streamMethod(streamMethod)
                .recordingCondition(recordingCondition)
                .title(title)
                .startDatetime(LocalDateTime.parse(startDatetime, Live.getLiveDatetimeFormatter()))
                .createDatetime(LocalDateTime.now())
                .tutor(tutor)
                .build();
    }
}
