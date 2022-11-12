package me.taling.live.api.internal.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.User;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class InternalRestUserUpdateRequest {

    @Schema(description = "추가할 (아마 결제 한) 유저목록")
    @NotNull
    private List<User> addUsers;

    @Schema(description = "삭제할 (아마 환불 한) 유저목록")
    @NotNull
    private List<User> removeUser;
}