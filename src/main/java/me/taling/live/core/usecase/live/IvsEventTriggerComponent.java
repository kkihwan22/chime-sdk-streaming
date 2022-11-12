package me.taling.live.core.usecase.live;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public interface IvsEventTriggerComponent {

    void execute(IvsEventParameter parameter);

    @Getter @ToString @EqualsAndHashCode
    class IvsEventParameter {
        private String channelName;
        private String streamId;
        private String recordingStatus;
        private String recordingStatusReason;
        private String bucketName;
        private String keyPrefix;

        public IvsEventParameter(String channelName,
                                 String streamId,
                                 String recordingStatus,
                                 String recordingStatusReason,
                                 String bucketName,
                                 String keyPrefix) {
            this.channelName = channelName;
            this.streamId = streamId;
            this.recordingStatus = recordingStatus;
            this.recordingStatusReason = recordingStatusReason;
            this.bucketName = bucketName;
            this.keyPrefix = keyPrefix;
        }
    }
}
