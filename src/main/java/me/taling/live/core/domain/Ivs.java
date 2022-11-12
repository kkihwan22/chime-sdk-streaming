package me.taling.live.core.domain;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
public class Ivs {
    @Setter
    private String liveId;
    private String channelArn;
    private String channelName;
    private String ingestEndpoint;
    private String streamUrl;
    private String streamKeyArn;
    private String streamKeyValue;
    private String ecsTaskId;

    public Ivs(String liveId, Ivs dummyIvs) {
        this.liveId = liveId;
        this.channelArn = dummyIvs.getChannelArn();
        this.channelName = dummyIvs.getChannelName();
        this.ingestEndpoint = dummyIvs.getIngestEndpoint();
        this.streamUrl = dummyIvs.getStreamUrl();
        this.streamKeyArn = dummyIvs.getStreamKeyArn();
        this.streamKeyValue = dummyIvs.getStreamKeyValue();
    }

    public Ivs withLiveId(String liveId) {
        this.liveId = liveId;
        return this;
    }

    public Ivs withEcsTaskId(String taskId) {
        this.ecsTaskId = taskId;
        return this;
    }
}
