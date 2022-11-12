package me.taling.live.core.application.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class SpaceBody<T> {

    private final String service = "live";
    private String type;
    private String channelId;
    private T data;

    public SpaceBody(String type, String channelId, T data) {
        this.type = type;
        this.channelId = channelId;
        this.data = data;
    }
}
