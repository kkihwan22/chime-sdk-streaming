package me.taling.live.core.usecase.live;

import lombok.*;
import me.taling.live.core.domain.Live;
import me.taling.live.global.vo.LiveMethod;

import java.time.LocalDateTime;

public interface CreateComponent {
    CreateResult execute(CreateParameter parameter);

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    @Getter
    class CreateParameter {
        private Long lessonId;
        private String title;
        private LiveMethod liveMethod;
        private Boolean recCondition;
        private LocalDateTime startDatetime;
        private String liveId;
        private String channelId;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    @Getter
    @Setter
    class CreateResult {
        private Live live;
    }
}
