package me.taling.live.api.internal.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.taling.live.api.attendee.response.ChimeResponse;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class BotJoinResponse {

    @Schema(description = "스페이스 채널 식별번호")
    private String channelId;

    @Schema(description = "chime model")
    private ChimeResponse joinInfo;
}
