package me.taling.live.api.query;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.query.response.EnrollmentResponse;
import me.taling.live.api.query.response.LiveResponse;
import me.taling.live.api.query.response.LivesResponse;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Lives;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.core.usecase.query.QueryComponent.EnrollmentsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static me.taling.live.attendee.domain.Attendee.AttendeeStatus.ATTENDED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lives")
public class QueryRestController {
    private final Logger log = LoggerFactory.getLogger(QueryRestController.class);
    private final QueryComponent queryComponent;

    @ApiOperation(value = "라이브목록", notes = "입장 가능한 라이브 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @GetMapping
    public SuccessResponseWrapper<List<LivesResponse>> lives() {
        List<Lives> results = queryComponent.lives();
        log.debug("results:{}", results);

        List<LivesResponse> response = results.stream()
                .map(result -> new LivesResponse(result))
                .collect(Collectors.toList());
        log.debug("response:{}", response);
        return SuccessResponseWrapper.success(response);
    }

    @ApiOperation(value = "라이브상세", notes = "요청 된 라이브 식별번호의 상세정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @ApiImplicitParam(name = "id", value = "라이브 식별번호(15자리)")
    @GetMapping("/{liveId}")
    public SuccessResponseWrapper<LiveResponse> live(@PathVariable String liveId) {
        Live result = queryComponent.live(liveId);
        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new LiveResponse(result));
    }

    @ApiOperation(value = "참석자목록", notes = "현재 클래스룸에 참여한 사용자 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @ApiImplicitParam(name = "id", value = "라이브 식별번호(15자리)")
    @GetMapping("/{liveId}/attendees")
    public SuccessResponseWrapper<List<AttendeeResponse>> attendees(
            @PathVariable String liveId) {
        // TODO: index.html 에서 조회 API 따로 사용해야할 듯
        Live live = queryComponent.live(liveId);
        List<Attendee> attendees = queryComponent.attendees(live, ATTENDED);
        List<AttendeeResponse> response = attendees.stream()
                .map(attendee -> new AttendeeResponse(attendee))
                .collect(Collectors.toList());
        log.debug("response:{}", response);
        return SuccessResponseWrapper.success(response);
    }

    @ApiOperation(value = "수강생목록", notes = "현재 클래스룸에 입장할 수 있는 사용자 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @ApiImplicitParam(name = "id", value = "라이브 식별번호(15자리)")
    @GetMapping("/{liveId}/enrollments")
    public SuccessResponseWrapper<EnrollmentResponse> enrollments(
            @PathVariable String liveId) {
        EnrollmentsResult result = queryComponent.users(liveId);
        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new EnrollmentResponse(result));
    }
}
