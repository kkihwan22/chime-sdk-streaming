package me.taling.live.infra.aws.ivs;

import com.amazonaws.services.ivs.AmazonIVSAsync;
import com.amazonaws.services.ivs.model.*;
import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Ivs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IvsClient {
    private final Logger log = LoggerFactory.getLogger(IvsClient.class);
    private final AmazonIVSAsync amazonIVS;
    private final IvsConfigurationProperties properties;

    public Ivs createChannel(String channelName, boolean recordingCondition) {
        CreateChannelRequest request = new CreateChannelRequest()
                .withName(channelName)
                .withType(properties.getChannelType())
                .withAuthorized(properties.isAuthorized())
                .withLatencyMode(properties.getChannelLatencyMode());

        if (recordingCondition) {
            request.withRecordingConfigurationArn(properties.getRecordingConfigurationArn());
        }

        log.debug("create channel. request:{}", request);
        CreateChannelResult result = amazonIVS.createChannel(request);
        log.debug("create channel. result:{}", result);

        return Ivs.builder()
                .channelArn(result.getChannel().getArn())
                .channelName(result.getChannel().getName())
                .ingestEndpoint(result.getChannel().getIngestEndpoint())
                .streamUrl(result.getChannel().getPlaybackUrl())
                .streamKeyArn(result.getStreamKey().getArn())
                .streamKeyValue(result.getStreamKey().getValue())
                .build();
    }

    public void deleteChannel(Ivs ivs) {
        log.debug("delete channel. ive:{}", ivs);
        String arn = ivs.getChannelArn();

        try {
            Stream stream = this.getStream(arn);
            if (stream.getState().equals("LIVE")) {
                log.debug("arn:{}", arn);
                amazonIVS.stopStream(new StopStreamRequest().withChannelArn(arn));
                String streamKeyArn = ivs.getStreamKeyArn();
                log.debug("stream key:{}", streamKeyArn);
                amazonIVS.deleteStreamKey(new DeleteStreamKeyRequest().withArn(streamKeyArn));
            }

            amazonIVS.deleteChannel(new DeleteChannelRequest().withArn(arn));
        } catch (Exception e) {
            log.error("delete channel error : {}", e);
        }
    }

    private Stream getStream(String arn) {
        GetStreamResult result = amazonIVS.getStream(new GetStreamRequest().withChannelArn(arn));
        Stream stream = result.getStream();
        log.debug("stream:{}", stream);
        return stream;
    }
}