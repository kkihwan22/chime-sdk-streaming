package me.taling.live.attendee.entrypoints;

import lombok.*;
import me.taling.live.api.asset.BaseRestController;
import me.taling.live.api.asset.response.DefaultResponse;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.attendee.request.ActionRequest;
import me.taling.live.core.usecase.action.ActionComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static me.taling.live.core.usecase.action.ActionComponent.Parameter;

@RequiredArgsConstructor
@RestController
public class ActionRestController implements BaseRestController {
    private final Logger log = LoggerFactory.getLogger(ActionRestController.class);
    private final ActionComponent actionComponent;

    @PatchMapping("/lives/{liveId}/actions/{actionType}")
    public SuccessResponseWrapper<DefaultResponse> action(@PathVariable String liveId,
                                                          @PathVariable String actionType,
                                                          @RequestBody ActionRequest request) {
        log.debug("request:{}", request);
        actionComponent.execute(Parameter.builder()
                .liveId(liveId)
                .actionType(actionType)
                .requester(ThreadLocalContextProvider.get())
                .userId(request.getUserId())
                .build());
        return SuccessResponseWrapper.success(new DefaultResponse(liveId));
    }

    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    class Response {
        private String liveId;
    }
}