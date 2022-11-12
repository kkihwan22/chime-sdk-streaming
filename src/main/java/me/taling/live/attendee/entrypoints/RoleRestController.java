package me.taling.live.attendee.entrypoints;

import lombok.*;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.attendee.applications.RoleUsecase;
import me.taling.live.attendee.domain.AttendeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RoleRestController {
    private final Logger log = LoggerFactory.getLogger(RoleRestController.class);
    private final RoleUsecase roleUsecase;

    @PatchMapping("/lives/{liveId}/attendees/{userId}/role/{attendeeType}")
    public SuccessResponseWrapper<Response> handleUrl(
                                            @PathVariable String liveId,
                                            @PathVariable Long userId,
                                            @PathVariable String attendeeType) {
        roleUsecase.execute(
                new RoleUsecase.RoleParameter(liveId, userId, AttendeeType.valueOf(attendeeType))
        );
        return new SuccessResponseWrapper<>(new Response(liveId));
    }

    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    class Response {
        private String liveId;
    }
}