package me.taling.live.api.internal.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.Ivs;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class IvsResponse {

    private String channelArn;

    private String channelName;

    private String ingestEndpoint;

    private String streamUrl;

    private String streamKeyArn;

    private String streamKeyValue;

    public IvsResponse(Ivs ivs) {
        this.channelArn = ivs.getChannelArn();
        this.channelName = ivs.getChannelName();
        this.ingestEndpoint = ivs.getIngestEndpoint();
        this.streamUrl = ivs.getStreamUrl();
        this.streamKeyArn = ivs.getStreamKeyArn();
        this.streamKeyValue = ivs.getStreamKeyValue();
    }
}
