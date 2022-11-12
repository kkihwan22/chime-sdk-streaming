package me.taling.live.infra.redis;

import me.taling.live.attendee.domain.Attendee;
import me.taling.live.global.vo.DeviceStatus;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("lives")
public class ActionRedisEntity extends BaseRedisEntity {

    @Id
    private String id;
    private DeviceStatus video;
    private DeviceStatus mic;
    private DeviceStatus hands;
    private DeviceStatus screen;

    public ActionRedisEntity(Attendee attendee) {
        this.id = new StringBuilder()
                .append(KEY_SEPARATOR)
                .append(attendee.getLive().getId())
                .append(KEY_SEPARATOR)
                .append(attendee.getUserId())
                .append(KEY_SEPARATOR)
                .append("actions")
                .toString();

        this.video = attendee.getVideoStatus();
        this.mic = attendee.getMicStatus();
        this.hands = attendee.getHandsStatus();
        this.screen = attendee.getScreenStatus();
    }
}
