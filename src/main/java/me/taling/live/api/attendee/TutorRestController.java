package me.taling.live.api.attendee;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.BaseRestController;
import me.taling.live.api.asset.response.DefaultResponse;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.core.usecase.tutor.TutorComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.taling.live.core.usecase.tutor.TutorComponent.Parameter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lives")
public class TutorRestController implements BaseRestController {
    private final Logger log = LoggerFactory.getLogger(TutorRestController.class);
    private final TutorComponent tutorComponent;

    @ApiOperation(value = "시작(강사입장)", notes = "수업을 시작한다. (강사입장)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @ApiImplicitParam(name = "liveId", value = "라이브 식별번호")
    @PatchMapping("/{liveId}/start")
    public SuccessResponseWrapper<DefaultResponse> startLive(
            @PathVariable String liveId) {
        // Do nothing. 프론트에서 호출하는 동선 제거 후 함께 제거
        return SuccessResponseWrapper.success(new DefaultResponse(liveId));
    }

    // TODO: Create, Close ---> LiveRestController 로 이동....
    @ApiOperation(value = "종료", notes = "진행 중인 라이브를 종료한다.")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParam(name = "liveId", value = "라이브 식별번호")
    @PatchMapping("/{liveId}/close")
    public SuccessResponseWrapper<DefaultResponse> closeLive(
            @PathVariable String liveId) {

        tutorComponent.closeLive(new Parameter(liveId));
        return SuccessResponseWrapper.success(new DefaultResponse(liveId));
    }

}