package me.taling.live.attendee.applications;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Live;
import org.springframework.stereotype.Component;

import static me.taling.live.global.vo.LiveType.LESSON;


@RequiredArgsConstructor
@Component
public class BeforeEnterLiveSpaceStrategyFactory {
    private final BeforeEnterLiveSpaceStrategy beforeEnterLivePublicSpaceStrategy;
    private final BeforeEnterLiveSpaceStrategy beforeEnterLivePrivateSpaceStrategy;

    public BeforeEnterLiveSpaceStrategy getStrategyBean(Live live) {
        if (live.getType() == LESSON) {
            return beforeEnterLivePrivateSpaceStrategy;
        }

        return beforeEnterLivePublicSpaceStrategy;
    }
}