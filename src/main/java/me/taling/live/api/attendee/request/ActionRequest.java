package me.taling.live.api.attendee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class ActionRequest {

    @Schema(description = "강사가 차단/해제하고자 하는 사용자의 ID")
    private Long userId;
}
