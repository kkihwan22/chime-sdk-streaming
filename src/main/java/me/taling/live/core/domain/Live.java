package me.taling.live.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;
import me.taling.live.global.vo.StreamMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Getter
public class Live {
    private final static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private String id;
    private LiveType type;
    private LiveMethod liveMethod;
    private StreamMethod streamMethod;
    private Boolean recordingCondition;

    private String title;
    private String channelId;
    private String meetingId;
    private User tutor;
    private List<User> managers;
    private List<User> tutees;

    private LocalDateTime createDatetime;
    private LocalDateTime expireDatetime;
    private LocalDateTime startDatetime;
    @Setter
    private LocalDateTime endDatetime;

    private Long expiration;

    private Ivs ivs;
    private String ecsTaskId;

    @JsonIgnore
    private Chime chime;



    public Live(String id) {
        this.id = id;
        this.createDatetime = LocalDateTime.now();
    }

    public static DateTimeFormatter getLiveDatetimeFormatter() {
        return Live.DATETIME_FORMATTER;
    }

    public Live withType(LiveType type) {
        this.type = type;
        return this;
    }

    public Live withLiveMethod(LiveMethod liveMethod) {
        this.liveMethod = liveMethod;
        return this;
    }

    public Live withStreamMethod(StreamMethod streamMethod) {
        this.streamMethod = streamMethod;
        return this;
    }

    public Live withRecordingCondition(Boolean recordingCondition) {
        this.recordingCondition = recordingCondition;
        return this;
    }

    public Live withTitle(String title) {
        this.title = title;
        return this;
    }

    public Live withMeetingId(String meetingId) {
        this.meetingId = meetingId;
        return this;
    }

    public Live withChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public Live withTutor(User tutor) {
        this.tutor = tutor;
        return this;
    }

    public Live withManagers(List<User> managers) {
        this.managers = managers;
        return this;
    }

    public Live withTutees(List<User> tutees) {
        this.tutees = tutees;
        return this;
    }

    public Live withIvs(Ivs ivs) {
        this.ivs = ivs;
        return this;
    }

    public Live withEcsTaskId(String ecsTaskId) {
        this.ecsTaskId = ecsTaskId;
        return this;
    }

    public Live withStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
        return this;
    }

    public Live withEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
        return this;
    }

    public Live withExpireDatetime(LocalDateTime expireDatetime) {
        this.expireDatetime = expireDatetime;
        return this;
    }

    public Live withChime(Chime chime) {
        this.chime = chime;
        return this;
    }

    public AttendeeType whatAttendeeType(Long userId) {
        if (tutor.isMatchedId(userId)) {
            return AttendeeType.TUTOR;
        }

        if (tutees != null) {
            for (User tutee : tutees) {
                if (tutee.isMatchedId(userId)) {
                    return AttendeeType.TUTEE;
                }
            }
        }

        return AttendeeType.GUEST;
    }

    public int enrolledCount() {
        int count = Optional.ofNullable(tutees).map(List::size).orElse(0);
        return count + 1;
    }
}