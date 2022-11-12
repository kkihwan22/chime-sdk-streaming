package me.taling.live.attendee.entrypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.attendee.response.ChimeResponse;
import me.taling.live.api.attendee.response.UserResponse;
import me.taling.live.attendee.applications.EnterLiveUsecase;
import me.taling.live.attendee.applications.EnterLiveUsecase.EnterLiveParameter;
import me.taling.live.attendee.applications.EnterLiveUsecase.EnterLiveResult;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.attendee.entrypoints.dto.EnterRequest;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.global.vo.DeviceStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_LIVE;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NO_PERMISSION;

@RequiredArgsConstructor
@RestController
public class EnterLiveRestController {
    private final Logger log = LoggerFactory.getLogger(EnterLiveRestController.class);
    private final QueryComponent queryComponent;
    private final EnterLiveUsecase usecase;

    @ApiOperation(value = "수업 입장 (학생)", notes = "라이브 수업에 입장한다. (학생)")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParam(name = "liveId", value = "라이브 식별번호(15자리)")
    @PutMapping("/lives/{liveId}/attendees")
    public SuccessResponseWrapper<Response> handleUrl(@PathVariable String liveId,
                                                      @RequestBody @Valid EnterRequest request) {

        // TODO: validation 하는 부분 변경.
        request.valid();
        String video = StringUtils.isBlank(request.getVideo()) ? null : request.getVideo().toUpperCase();
        String audio = StringUtils.isBlank(request.getAudio()) ? null : request.getAudio().toUpperCase();

        Live live = Optional.ofNullable(queryComponent.live(liveId))
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));
        User requester = Optional.ofNullable(ThreadLocalContextProvider.get())
                .orElseThrow(() -> new LiveException(NO_PERMISSION));
        log.debug("[Enter] live:{}, request:{}, body:{}", live.getId(), requester.getId(), request);

        EnterLiveResult result = usecase.execute(new EnterLiveParameter(
                live,
                requester,
                video == null ? null : DeviceStatus.from(video),
                video == null ? null : DeviceStatus.from(audio))
        );

        Response response = new Response(live, result.getAttendee(), result.getChime());
        log.debug("response:{}", response);
        return SuccessResponseWrapper.success(response);
    }

    @Getter @ToString
    class Response {
        @Schema(description = "라이브 식별번호 (15자리)")
        private String liveId;

        @Schema(description = "라이브 진행방식 [INTERACTIVE | WEBINAR]")
        private String liveMethod;

        @Schema(description = "채널 아이디")
        private String channelId;

        @Schema(description = "Chime Meeting Id")
        private String meetingId;

        @Schema(description = "참석자 정보")
        private AttendeeResponse attendee;

        @Schema(description = "튜터정보")
        private UserResponse tutor;

        @Schema(description = "라이브 타이틀")
        private String title;

        @Schema(description = "수강신청자 수")
        private Integer enrolledCount;

        @Schema(description = "라이브 수업 종료시간")
        private LocalDateTime endDatetime;

        @Schema(description = "라이브 완전 종료시간")
        private LocalDateTime expireDatetime;

        @Schema(description = "*** chime 접속정보 (meeting, attendee) ***")
        @JsonProperty("joinInfo")
        private ChimeResponse chime;

        public Response(Live live, Attendee attendee, Chime chime) {
            this.liveId = live.getId();
            this.liveMethod = live.getLiveMethod().name();
            this.channelId = live.getChannelId();
            this.meetingId = live.getMeetingId();
            this.attendee = new AttendeeResponse(attendee);
            this.tutor = new UserResponse(live.getTutor());
            this.title = live.getTitle();
            this.enrolledCount = live.enrolledCount();
            this.endDatetime = live.getEndDatetime();
            this.expireDatetime = live.getExpireDatetime();
            this.chime = new ChimeResponse(chime);
        }
    }
}