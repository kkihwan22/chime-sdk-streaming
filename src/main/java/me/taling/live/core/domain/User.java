package me.taling.live.core.domain;

import lombok.*;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.global.vo.ConnectDevice;

@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {
        "nickname", "imageUrl", "thumbnailUrl", "isUsingPushNotification", "connectDevice"
})
@Getter
public class User {
    private Long id;
    private String nickname;
    private String imageUrl;
    private String thumbnailUrl;
    private Boolean isUsingPushNotification;
    private ConnectDevice connectDevice;

    @Builder
    public User(Long id, String nickname, String imageUrl, String thumbnailUrl, Boolean isUsingPushNotification, ConnectDevice connectDevice) {
        this.id = id;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.isUsingPushNotification = isUsingPushNotification;
        this.connectDevice = connectDevice;
    }

    public User(Attendee attendee) {
        this.id = attendee.getUserId();
        this.nickname = attendee.getNickname();
        this.imageUrl = attendee.getImageUrl();
        this.thumbnailUrl = attendee.getThumbnailUrl();
    }

    public boolean isMatchedId(Long id) {
        return this.id.equals(id);
    }
}