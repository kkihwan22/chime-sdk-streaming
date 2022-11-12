package me.taling.live.api.internal.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class InternalLiveCreateRequest {

    @NotNull
    @Schema(description = "라이브 식별번호")
    private String liveId;

    @NotNull
    @Schema(description = "스페이스 식별번호")
    private String channelId;

    @NotNull
    @Schema(description = "라이브 젝목")
    private String title;

    @NotEmpty
    @Pattern(regexp = "INTERACTION|WEBINAR")
    @Schema(description = "라이브 방식 (양방향, 웨비나)")
    private String liveMethod;

    @Schema(description = "레코딩 여부")
    private Boolean recCondition;

    @NotNull
    @Schema(description = "수업시작 시간 (yyyy-MM-ddTHH:mm:ss)")
    private LocalDateTime startedAt;

    @NotNull
    @Schema(description = "수업종료 시간 (yyyy-MM-ddTHH:mm:ss)")
    private LocalDateTime expiredAt;
}