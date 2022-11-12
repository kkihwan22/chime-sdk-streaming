package me.taling.live.infra.redis;

import lombok.*;
import me.taling.live.attendee.domain.Actions;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.Attendee.AttendeeStatus;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.global.vo.ConnectDevice;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash("live-attendee")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AttendeeRedisEntity extends BaseRedisEntity{

    // TODO: ID 구조도 변경
    // TODO: STATUS 를 @Indexed로 관리하기가 불가능.

    @Id
    private String id;
    @Indexed
    private String liveId;
    @Indexed
    private Long userId;
    private AttendeeType attendeeType;
    private String nickname;
    private String imageUrl;
    private String thumbnailUrl;
    @Indexed
    private String attendeeId;
    private Boolean isUsingPushNotification;
    private ConnectDevice connectDevice;
    private AttendeeStatus status;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    private LocalDateTime attendedWaitingroomDatetime;
    private LocalDateTime attendedDatetime;
    private LocalDateTime exitLiveDatetime;

    private Actions actions;

    public AttendeeRedisEntity(Attendee attendee) {
        this.id = makeId(attendee.getLive().getId(), attendee.getUserId());
        this.liveId = attendee.getLive().getId();
        this.userId = attendee.getUserId();
        this.attendeeType = attendee.getAttendeeType();
        this.nickname = attendee.getNickname();
        this.imageUrl = attendee.getImageUrl();
        this.thumbnailUrl = attendee.getThumbnailUrl();
        this.attendeeId = attendee.getAttendeeId();
        this.isUsingPushNotification = attendee.getIsUsingPushNotification();
        this.connectDevice = attendee.getConnectDevice();
        this.status = attendee.getStatus();
        this.createdDatetime = attendee.getCreatedDatetime();
        this.updatedDatetime = attendee.getUpdatedDatetime();
        this.attendedWaitingroomDatetime = attendee.getAttendedWaitingroomDatetime();
        this.attendedDatetime = attendee.getAttendedDatetime();
        this.exitLiveDatetime = attendee.getExitLiveDatetime();
        this.actions = attendee.getActions();
    }

    public static String makeId(String liveId, Long userId) {
        return new StringBuilder()
                .append(liveId)
                .append(KEY_SEPARATOR)
                .append(userId)
                .toString();
    }

    public Attendee of(Live live) {
        // Abstract Attendee 로 변경 시, Actions에 대한 생성자 추가 .
        Attendee attendee = new Attendee(
                live,
                new User(this.userId, this.nickname, this.imageUrl, this.thumbnailUrl, this.isUsingPushNotification, this.connectDevice),
                this.attendeeType,
                this.status,
                this.attendeeId);
        attendee.setActions(this.actions);
        return attendee;
    }
}