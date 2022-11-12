package me.taling.live.api.space;

import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.domain.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SpaceRestController {
    private final Logger log = LoggerFactory.getLogger(SpaceRestController.class);
    private final SpaceComponent spaceComponent;

    @GetMapping("/space/{channelId}")
    public SuccessResponseWrapper<SpaceResponse> getSpace(@PathVariable String channelId) {
        Space space = spaceComponent.findOne(channelId);
        log.debug("find space:{}", space);
        return SuccessResponseWrapper.success(new SpaceResponse(space));
    }
}
