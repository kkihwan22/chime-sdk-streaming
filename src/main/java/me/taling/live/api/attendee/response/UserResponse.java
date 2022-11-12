package me.taling.live.api.attendee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.core.domain.User;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class UserResponse {
    @Schema(description = "탈잉 사용자 식별번호")
    private Long userId;

    @Schema(description = "탈잉 사용자 닉네임")
    private String nickname;

    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;

    @Schema(description = "프로필 썸네일 URL")
    private String profileThumbnailUrl;

    @Schema(description = "참석자타입")
    private AttendeeType attendeeType;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getImageUrl();
        this.profileThumbnailUrl = user.getThumbnailUrl();
    }
}