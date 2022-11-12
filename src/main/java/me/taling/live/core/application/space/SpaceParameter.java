package me.taling.live.core.application.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.User;
import me.taling.live.infra.feigns.space.SpaceFeignUser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@Getter
public class SpaceParameter {
    private String referenceId;
    private String referenceType;
    private String title;
    private List<SpaceFeignUser> owners;
    private List<SpaceFeignUser> users;
    private List<SpaceFeignUser> managers;
    private Boolean isPublic;
    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;

    public SpaceParameter(String referenceId, String referenceType, String title, User tutor, List<User> managers, List<User> tutees, Boolean isPublic) {
        this(referenceId, referenceType, title, tutor, managers, tutees, isPublic, null, null);
    }

    public SpaceParameter(String referenceId, String referenceType, String title, User tutor, List<User> managers, List<User> tutees, Boolean isPublic, LocalDateTime startedAt, LocalDateTime expiredAt) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.title = title;
        this.owners = Collections.singletonList(new SpaceFeignUser(tutor));
        this.managers = managers.stream()
                .map(user -> new SpaceFeignUser(user))
                .collect(Collectors.toList());
        this.users = tutees.stream()
                .map(user -> new SpaceFeignUser(user))
                .collect(Collectors.toList());
        this.isPublic = isPublic;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
    }
}
