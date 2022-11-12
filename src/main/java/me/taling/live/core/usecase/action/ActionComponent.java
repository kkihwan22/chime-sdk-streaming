package me.taling.live.core.usecase.action;

import lombok.*;
import me.taling.live.core.domain.User;

public interface ActionComponent {

    void execute(Parameter parameter);

    @AllArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    @Getter
    class Parameter {
        private String liveId;
        private String actionType;
        private User requester;
        private Long userId;
    }
}
