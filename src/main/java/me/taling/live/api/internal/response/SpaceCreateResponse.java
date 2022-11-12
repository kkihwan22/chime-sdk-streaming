package me.taling.live.api.internal.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.Space;

@ToString
@EqualsAndHashCode
@Getter
public class SpaceCreateResponse {

    @Schema(description = "스페이스 식별번호")
    private String spaceId;

    public SpaceCreateResponse(Space space) {
        this.spaceId = space.getChannelId();
    }
}
