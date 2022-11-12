package me.taling.live.core.usecase.attendee;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceParameter;
import me.taling.live.core.domain.Space;
import me.taling.live.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UpdateAttendeesComponentImpl implements UpdateAttendeesComponent {
    private final Logger log = LoggerFactory.getLogger(UpdateAttendeesComponentImpl.class);
    private final SpaceComponent spaceComponent;

    @Override
    public UpdateAttendeesResult execute(UpdateAttendeesParameter parameter) {
        log.debug("parameter:{}", parameter);

        String channelId = parameter.getLive().getChannelId();
        Space space = spaceComponent.findOne(channelId);
        log.debug("find space:{}", space);

        List<User> tutees = space.getTutees();
        parameter.getRemoveUsers().stream()
                .forEach(removeUser -> tutees.remove(removeUser));

        parameter.getAddUsers().stream()
                .forEach(addUser -> tutees.add(addUser));

        log.debug("tutees:{}", tutees);
        spaceComponent.put(channelId, new SpaceParameter(
                space.getReferenceId(),
                space.getReferenceType(),
                space.getTitle(),
                space.getTutor(),
                space.getManagers(),
                space.getTutees(),
                space.getIsPublic())
        );

        return new UpdateAttendeesResult(parameter.getLive().getId());
    }
}
