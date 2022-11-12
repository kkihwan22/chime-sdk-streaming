package me.taling.live.api.space;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.taling.live.core.domain.Space;
import me.taling.live.core.domain.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Getter
public class SpaceResponse {
    @Schema(description = "스페이스 채널 식별번호")
    private String channelId;

    @Schema(description = "스페이스 참고 식별번호")
    private String referenceId;

    @Schema(description = "스페이스 타입 (LIVE|VOD)")
    private String referenceType;

    @Schema(description = "스페이스 제목")
    private String title;

    @Schema(description = "강사정보")
    private User tutor;

    @Schema(description = "매니저 참가자 목록")
    private List<User> managers;

    @Schema(description = "수강생 참가자 목록")
    private List<User> tutees;

    @Schema(description = "채널 공개/비공개 여부")
    private Boolean isPublic;

    public SpaceResponse(Space space) {
        this.channelId = space.getChannelId();
        this.referenceId = space.getReferenceId();
        this.referenceType = space.getReferenceType();;
        this.title = space.getTitle();
        this.tutor = space.getTutor();
        this.managers = space.getManagers();
        this.tutees = space.getTutees();
        this.isPublic = space.getIsPublic();
    }
}
