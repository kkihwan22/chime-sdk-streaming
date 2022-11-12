package me.taling.live.infra.feigns.space;

import lombok.*;
import me.taling.live.core.application.space.SpaceUser;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SpaceUserRequest {
    private Long id;
    private String nickname;
    private String role;
    private String imageUrl;
    private String thumbnailUrl;
    private String status;
    private Boolean isUsingPushNotification;
    private Boolean isLinkedLive;
    private Boolean isLinkedStandaloneSpace;

    public SpaceUserRequest(SpaceUser user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.role = user.getRole().getCode();
        this.imageUrl = user.getImageUrl();
        this.thumbnailUrl = user.getThumbnailUrl();
        this.status = user.getStatus().getCode();
        this.isUsingPushNotification = user.getIsUsingPushNotification();
        this.isLinkedLive = user.getIsLinkedLive();
        this.isLinkedStandaloneSpace = user.getIsLinkedStandaloneSpace();
    }
}
