package me.taling.live.global.entity;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
@RedisHash("meetings")
public class MeetingEntity {

    @Id
    private String id;
    private String liveId;
}
