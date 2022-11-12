package me.taling.live.api.internal.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class IvsEventRequest {

    @JsonProperty("channel_name")
    private String channelName;

    @JsonProperty("stream_id")
    private String streamId;

    @JsonProperty("recording_status")
    private String recordingStatus;

    @JsonProperty("recording_status_reason")
    private String recordingStatusReason;

    @JsonProperty("recording_s3_bucket_name")
    private String bucketName;

    @JsonProperty("recording_s3_key_prefix")
    private String keyPrefix;
}