package me.taling.live.api.create.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.usecase.live.CreateComponent.CreateResult;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class CreateResponse {
    @Schema(description = "라이브 식별번호(15자리)")
    private String liveId;

    public CreateResponse(CreateResult result) {
        this.liveId = result.getLive().getId();
    }
}