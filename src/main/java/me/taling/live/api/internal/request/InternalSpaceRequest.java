package me.taling.live.api.internal.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.core.domain.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class InternalSpaceRequest {

    @NotBlank
    @Schema(description = "스페이스 식별자 (탈렌트 관련 유일한 정보)")
    private String referenceId;

    @NotBlank
    @Schema(description = "스페이스 타입")
    private String referenceType;

    @NotBlank
    @Schema(description = "스페이스 제목")
    private String title;

    @Schema(description = "강사")
    private User tutor;

    @Schema(description = "스탭 목록")
    private List<User> managers;

    @Schema(description = "수강생 목록")
    private List<User> users;

    @Schema(description = "space 시작일시 (yyyy-MM-ddThh:mm:ss)")
    private LocalDateTime startedAt;

    @Schema(description = "space 만료일시 (yyyy-MM-ddThh:mm:ss)")
    private LocalDateTime expiredAt;
}