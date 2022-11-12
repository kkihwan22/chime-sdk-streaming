package me.taling.live.api.asset.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class DefaultResponse {
    private String liveId;
}
