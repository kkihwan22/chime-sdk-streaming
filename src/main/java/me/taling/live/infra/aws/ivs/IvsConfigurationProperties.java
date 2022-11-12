package me.taling.live.infra.aws.ivs;

import lombok.*;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class IvsConfigurationProperties {

    private String channelType;
    private String channelLatencyMode;
    private boolean authorized;
    private String recordingConfigurationArn;
}