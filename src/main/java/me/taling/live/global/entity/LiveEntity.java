package me.taling.live.global.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;
import me.taling.live.global.vo.StreamMethod;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@RedisHash("lives")
public class LiveEntity {

    @Id
    private String id;
    private LiveType type;
    private LiveMethod liveMethod;
    private StreamMethod streamMethod;
    private Boolean recordingCondition;
    private String channelId;
    private String meetingId;
    private LocalDateTime createDatetime;
    private LocalDateTime expireDatetime;
    private LocalDateTime endDatetime;
    private LocalDateTime startDatetime;

    private String title;
    private User tutor;
    private List<User> tutees;

    private Ivs ivs;
    private String ecsTaskId;

    public LiveEntity(Live live) {
        this.id = live.getId();
        this.type = live.getType();
        this.liveMethod = live.getLiveMethod();
        this.streamMethod = live.getStreamMethod();
        this.recordingCondition = live.getRecordingCondition();

        this.title = live.getTitle();
        this.channelId = live.getChannelId();
        this.meetingId = live.getMeetingId();
        this.tutor = live.getTutor();
        this.tutees = live.getTutees();

        this.createDatetime = live.getCreateDatetime();
        this.endDatetime = live.getEndDatetime();
        this.expireDatetime = live.getExpireDatetime();
        this.startDatetime = live.getStartDatetime();

        this.ivs = live.getIvs();
        this.ecsTaskId = live.getEcsTaskId();
    }

    public Live toLive() {
        return Live.builder()
                .id(id)
                .type(type)
                .liveMethod(liveMethod)
                .streamMethod(streamMethod)
                .recordingCondition(recordingCondition)
                .title(title)
                .meetingId(meetingId)
                .channelId(channelId)
                .createDatetime(createDatetime)
                .expireDatetime(expireDatetime)
                .endDatetime(endDatetime)
                .startDatetime(startDatetime)
                .tutor(tutor)
                .tutees(tutees)
                .ivs(ivs)
                .ecsTaskId(ecsTaskId)
                .build();
    }
}
