package me.taling.live.core.domain;

import lombok.*;
import me.taling.live.global.vo.ConnectDevice;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
public class TalingHttpHeader {
    private ConnectDevice connectDevice;
}
