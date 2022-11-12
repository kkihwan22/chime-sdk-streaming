package me.taling.live.global.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@RedisHash("attendees")
public class AttendeeEntity {
    @Id
    private String id;
    private Map<Long, Attendee> attendeeMap;

    public AttendeeEntity(String liveId, Map<Long, Attendee> attendeeMap) {
        this.id = liveId;
        this.attendeeMap = attendeeMap;
    }
}
