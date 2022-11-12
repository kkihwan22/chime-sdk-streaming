package me.taling.live.api.query.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.taling.live.api.attendee.response.UserResponse;
import me.taling.live.core.domain.Live;
import me.taling.live.global.vo.LiveMethod;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Getter
public class LiveResponse {

    @Schema(description = "라이브 식별번호")
    private String liveId;

    @Schema(description = "channel 식별번호")
    private String channelId;

    @Schema(description = "라이브 진행 방식 추가 ")
    private LiveMethod liveMethod;

    @Schema(description = "aws chime 미팅아이디")
    private String meetingId;

    @Schema(description = "라이브 제목")
    private String title;

    @Schema(description = "강사정보")
    private UserResponse tutor;

    public LiveResponse(Live live) {
        this.liveId = live.getId();
        this.channelId = live.getChannelId();
        this.liveMethod = live.getLiveMethod();
        this.meetingId = live.getMeetingId();
        this.title = live.getTitle();
        this.tutor = new UserResponse(live.getTutor());
    }
}
