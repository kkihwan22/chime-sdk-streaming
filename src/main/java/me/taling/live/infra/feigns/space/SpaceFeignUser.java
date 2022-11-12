package me.taling.live.infra.feigns.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.User;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class SpaceFeignUser {
    private Long id;
    private String nickname;
    private String imageUrl;
    private String thumbnailUrl;
    private Boolean isUsingPushNotification;

    public SpaceFeignUser(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.imageUrl = user.getImageUrl();
        this.thumbnailUrl = user.getThumbnailUrl();
        this.isUsingPushNotification = user.getIsUsingPushNotification();
    }
}
