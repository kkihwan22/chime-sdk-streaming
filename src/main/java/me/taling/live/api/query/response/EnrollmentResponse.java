package me.taling.live.api.query.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.api.attendee.response.UserResponse;
import me.taling.live.core.usecase.query.QueryComponent.EnrollmentsResult;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class EnrollmentResponse {

    @Schema(description = "강사")
    private UserResponse tutor;

    @Schema(description = "학생 목록")
    private List<UserResponse> tutees;

    public EnrollmentResponse(EnrollmentsResult result) {
        this.tutor = new UserResponse(result.getTutor());
        this.tutees = result.getTutees().stream().map(tutee -> new UserResponse(tutee)).collect(Collectors.toList());
    }
}
