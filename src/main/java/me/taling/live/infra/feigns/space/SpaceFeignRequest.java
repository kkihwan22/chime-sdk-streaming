package me.taling.live.infra.feigns.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.application.space.SpaceParameter;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
public class SpaceFeignRequest {
    private String type = "CHAT";
    private String referenceType = "MEETING";
    private String referenceId;
    private String title;
    private List<SpaceFeignUser> owners;
    private List<SpaceFeignUser> users;
    private List<SpaceFeignUser> managers;
    private Boolean isPublic;

    public SpaceFeignRequest(SpaceParameter parameter) {
        this.referenceId = parameter.getReferenceId();
        this.referenceType = parameter.getReferenceType();
        this.title = parameter.getTitle();
        this.owners = parameter.getOwners();
        this.users = parameter.getUsers();
        this.managers = parameter.getManagers();
        this.isPublic = parameter.getIsPublic();
    }
}