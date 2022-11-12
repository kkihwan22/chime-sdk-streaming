package me.taling.live.api.query.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.taling.live.core.domain.Lives;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class LivesResponse {
    @Schema(description = "라이브 식별번호")
    private String liveId;

    @Schema(description = "라이브 제목")
    private String title;

    @Schema(description = "스페이스 채널 아이디")
    private String channelId;

    @Schema(description = "생성일시")
    private LocalDateTime createdDatetime;

    public LivesResponse(Lives lol) {
        this.liveId = lol.getLiveId();
        this.title = lol.getTitle();
        this.channelId = lol.getChannelId();
        this.createdDatetime = lol.getCreatedDatetime();
    }
}
