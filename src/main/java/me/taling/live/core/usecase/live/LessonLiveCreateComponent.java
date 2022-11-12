package me.taling.live.core.usecase.live;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.global.vo.LiveMethod;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonLiveCreateComponent {

    LessonLiveCreateResult execute(LessonLiveCreateParameter parameter);

    @ToString
    @EqualsAndHashCode
    @Getter
    class LessonLiveCreateParameter {
        private String liveId;
        private String channelId;
        private String title;
        private LiveMethod liveMethod;
        private Boolean recCondition;
        private LocalDateTime startedAt;
        private LocalDateTime expiredAt;

        public LessonLiveCreateParameter(String liveId, String channelId, String title, String liveMethod, Boolean recCondition, LocalDateTime startedAt, LocalDateTime expiredAt) {
            this.liveId = liveId;
            this.channelId = channelId;
            this.title = title;
            this.liveMethod = LiveMethod.valueOf(liveMethod);
            this.recCondition = Optional.ofNullable(recCondition).orElse(Boolean.FALSE);
            this.startedAt = startedAt;
            this.expiredAt = expiredAt;
        }
    }


    @ToString
    @EqualsAndHashCode
    @Getter
    class LessonLiveCreateResult {
        private String liveId;

        public LessonLiveCreateResult(String liveId) {
            this.liveId = liveId;
        }
    }

}
