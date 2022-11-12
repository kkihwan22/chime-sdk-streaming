package me.taling.live.api.create.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.usecase.live.CreateComponent.CreateParameter;
import me.taling.live.global.vo.LiveMethod;
import me.taling.live.global.vo.LiveType;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class CreateRequest {

    @Schema(description = "라이브 아이디")
    private String liveId;

    @Schema(description = "스페이스 생성 아이디")
    private String channelId;

    @Schema(description = "미팅방 이름")
    private String title;

    @Schema(description = "레코딩 여부")
    private Boolean recCondition;

    @Schema(description = "라이브 생성 타입 (INSTANCE: 즉각생성 | LESSON: 수업에 의해 생성 | MEETING: 정해진 시간에 생성)")
    private LiveType liveType;

    @Schema(description = "라이브 방식 (INTERACTIVE | WEBINAR)")
    private LiveMethod liveMethod;

    @Schema(description = "liveType = MEETING 인 경우, 시작시간 (yyyy-MM-dd HH:mm:ss)")
    private LocalDateTime startDatetime;

    public CreateParameter of() {
        return CreateParameter.builder()
                .liveId(liveId)
                .channelId(channelId)
                .title(title)
                .liveMethod(liveMethod)
                .recCondition(recCondition)
                .startDatetime(startDatetime)
                .build();
    }
}
