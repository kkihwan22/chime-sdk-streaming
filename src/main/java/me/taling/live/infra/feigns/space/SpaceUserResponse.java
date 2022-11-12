package me.taling.live.infra.feigns.space;

import lombok.*;
import me.taling.live.core.application.space.SpaceUser;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SpaceUserResponse {
    private Long id;
    private String nickname;
    private String role;
    private String imageUrl;
    private String thumbnailUrl;
    private String status;
    private Boolean isUsingPushNotification;
    private Boolean isLinkedLive;
    private Boolean isLinkedStandaloneSpace;

    public SpaceUser of() {
        return new SpaceUser(
                id,
                nickname,
                role,
                imageUrl,
                thumbnailUrl,
                status,
                isUsingPushNotification,
                isLinkedLive,
                isLinkedStandaloneSpace);
    }
}