package me.taling.live.core.application.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.User;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static me.taling.live.core.application.space.SpaceUser.Role.MANAGER;
import static me.taling.live.core.application.space.SpaceUser.Role.USER;

@Getter
@ToString
@EqualsAndHashCode
public class SpaceUser {

    public enum Role {
        OWNER("owner"), MANAGER("manager"), USER("user");
        @Getter
        private String code;

        Role(String code) {
            this.code = code;
        }

        private static final Map<String, Role> map =
                Arrays.stream(Role.values()).collect(Collectors.toMap(Role::getCode, Function.identity()));

        public static Role from(String key) {
            return ofNullable(map.get(key))
                    .orElseThrow(() -> new IllegalArgumentException());
        }
    }

    public enum Status {
        READY("ready"), ACTIVE("active"), INACTIVE("inactive"),
        ;
        @Getter
        private String code;

        Status(String code) {
            this.code = code;
        }

        private static final Map<String, Status> map =
                Arrays.stream(Status.values()).collect(Collectors.toMap(Status::getCode, Function.identity()));

        public static Status from(String key) {
            return ofNullable(map.get(key))
                    .orElseThrow(() -> new IllegalArgumentException());
        }
    }

    private Long id;
    private String nickname;
    private Role role;
    private String imageUrl;
    private String thumbnailUrl;
    private Status status;
    private Boolean isUsingPushNotification;
    private Boolean isLinkedLive;
    private Boolean isLinkedStandaloneSpace;

    public SpaceUser(Long id, String nickname, String role, String imageUrl, String thumbnailUrl, String status, Boolean isUsingPushNotification, Boolean isLinkedLive, Boolean isLinkedStandaloneSpace) {
        this.id = id;
        this.nickname = nickname;
        this.role = Role.from(role);
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.status = Status.from(status);
        this.isUsingPushNotification = isUsingPushNotification;
        this.isLinkedLive = isLinkedLive;
        this.isLinkedStandaloneSpace = isLinkedStandaloneSpace;
    }

    public void changedRoleForManager() {
        this.role = MANAGER;
    }

    public void changedRoleForUser() {
        this.role = USER;
    }

    public User toUser() {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .thumbnailUrl(thumbnailUrl)
                .isUsingPushNotification(isUsingPushNotification)
                .build();
    }
}