package me.taling.live.attendee.entrypoints;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.attendee.applications.ExitLiveUsecase;
import me.taling.live.attendee.applications.dto.AttendeeParameter;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_LIVE;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NO_PERMISSION;

@RequiredArgsConstructor
@RestController
public class ExitLiveRestController {
    private final Logger log = LoggerFactory.getLogger(ExitLiveRestController.class);
    private final QueryComponent queryComponent;
    private final ExitLiveUsecase exitLiveUsecase;

    @ApiOperation(value = "나가기", notes = "진행 중인 라이브에서 나간다.")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParam(name = "liveId", value = "라이브 식별번호")
    @PatchMapping("/lives/{liveId}/exit")
    public SuccessResponseWrapper<Response> handleUrl(@PathVariable String liveId) {
        Live live = Optional.ofNullable(queryComponent.live(liveId))
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));
        User requester = Optional.ofNullable(ThreadLocalContextProvider.get())
                .orElseThrow(() -> new LiveException(NO_PERMISSION));

        exitLiveUsecase.execute(new AttendeeParameter(live, requester));
        return SuccessResponseWrapper.success(new Response(liveId));
    }

    @Getter @ToString
    class Response {
        private String liveId;

        public Response(String liveId) {
            this.liveId = liveId;
        }
    }
}
