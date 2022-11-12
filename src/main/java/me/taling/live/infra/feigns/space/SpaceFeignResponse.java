package me.taling.live.infra.feigns.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.Space;
import me.taling.live.core.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class SpaceFeignResponse {
    private String id;
    private String type;
    private String referenceId;
    private String referenceType;
    private String title;
    private List<SpaceFeignUser> owners;
    private List<SpaceFeignUser> users;
    private List<SpaceFeignUser> managers;
    private Boolean isPublic;

    public Space toSpace() {
        return new Space(
                id,
                referenceId,
                referenceType,
                title,
                toUser(owners.get(0)),
                managers.stream().map(user -> toUser(user)).collect(Collectors.toList()),
                users.stream().map(user -> toUser(user)).collect(Collectors.toList()),
                isPublic
        );
    }

    public User toUser(SpaceFeignUser spaceFeignUser) {
        return User.builder()
                .id(spaceFeignUser.getId())
                .nickname(spaceFeignUser.getNickname())
                .imageUrl(spaceFeignUser.getImageUrl())
                .thumbnailUrl(spaceFeignUser.getThumbnailUrl())
                .isUsingPushNotification(spaceFeignUser.getIsUsingPushNotification())
                .build();
    }
}
