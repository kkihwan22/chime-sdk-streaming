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

    @ApiOperation(value = "?????? ?????? (??????)", notes = "????????? ????????? ????????????. (??????)")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParam(name = "liveId", value = "????????? ????????????(15??????)")
    @PutMapping("/lives/{liveId}/attendees")
    public SuccessResponseWrapper<Response> handleUrl(@PathVariable String liveId,
                                                      @RequestBody @Valid EnterRequest request) {

        // TODO: validation ?????? ?????? ??????.
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
        @Schema(description = "????????? ???????????? (15??????)")
        private String liveId;

        @Schema(description = "????????? ???????????? [INTERACTIVE | WEBINAR]")
        private String liveMethod;

        @Schema(description = "?????? ?????????")
        private String channelId;

        @Schema(description = "Chime Meeting Id")
        private String meetingId;

        @Schema(description = "????????? ??????")
        private AttendeeResponse attendee;

        @Schema(description = "????????????")
        private UserResponse tutor;

        @Schema(description = "????????? ?????????")
        private String title;

        @Schema(description = "??????????????? ???")
        private Integer enrolledCount;

        @Schema(description = "????????? ?????? ????????????")
        private LocalDateTime endDatetime;

        @Schema(description = "????????? ?????? ????????????")
        private LocalDateTime expireDatetime;

        @Schema(description = "*** chime ???????????? (meeting, attendee) ***")
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