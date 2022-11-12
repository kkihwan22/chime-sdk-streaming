package me.taling.live.global.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@RedisHash("ivs")
public class IvsEntity {

    @Id
    private String id;
    private String liveId;
}
