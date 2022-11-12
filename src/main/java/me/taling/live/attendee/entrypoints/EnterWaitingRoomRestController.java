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
import me.taling.live.attendee.applications.EnterWaitingRoomUsecase;
import me.taling.live.attendee.applications.dto.AttendeeParameter;
import me.taling.live.attendee.applications.dto.AttendeeResult;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.entrypoints.dto.AttendeeResponse;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.taling.live.attendee.domain.Attendee.AttendeeStatus.ATTENDED;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_LIVE;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.NO_PERMISSION;

@RequiredArgsConstructor
@RestController
public class EnterWaitingRoomRestController {
    private final DateTimeFormatter START_TIME_FORMAT = DateTimeFormatter.ofPattern("MM월 dd일 (E) a hh시 mm분");
    private final DateTimeFormatter END_TIME_FORMAT = DateTimeFormatter.ofPattern("a hh시 mm분");

    private final Logger log = LoggerFactory.getLogger(EnterWaitingRoomRestController.class);
    private final QueryComponent queryComponent;
    private final EnterWaitingRoomUsecase enterWaitingRoomUsecase;

    @ApiOperation(value = "대기실 입장", notes = "Aws chime 의 JsonInfo 정보 전달")
    @ApiResponse(code = 200, message = "success")
    @ApiImplicitParam(name = "liveId", value = "라이브 식별번호(15자리)")
    @PutMapping("/lives/{liveId}/waiting-room")
    public SuccessResponseWrapper<Response> enterToWaitingroom(@PathVariable String liveId) {
        Live live = Optional.ofNullable(queryComponent.live(liveId))
                .orElseThrow(() -> new LiveException(NOT_FOUND_LIVE));
        User requester = Optional.ofNullable(ThreadLocalContextProvider.get())
                .orElseThrow(() -> new LiveException(NO_PERMISSION));

        AttendeeResult result = enterWaitingRoomUsecase.execute(new AttendeeParameter(live, requester));
        // TODO: Attendees 구하는 로직 개발..
        return SuccessResponseWrapper.success(new Response(live, result.getAttendee(), result.getChime(), queryComponent.attendees(live, ATTENDED)));
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

        @Schema(description = "라이브 일정(시작~종료)")
        private String displayDatetime;

        @Schema(description = "수강신청자 수")
        private Integer enrolledCount;

        @Schema(description = "현재참석자 목록")
        private List<AttendeeResponse> attendees;

        @Schema(description = "*** chime 접속정보 (meeting, attendee) ***")
        @JsonProperty("joinInfo")
        private ChimeResponse chime;

        public Response(Live live, Attendee attendee, Chime chime, List<Attendee> attendees) {
            this.liveId = live.getId();
            this.liveMethod = live.getLiveMethod().name();
            this.channelId = live.getChannelId();
            this.meetingId = live.getMeetingId();
            this.title = live.getTitle();

            // TODO: 요고 쓰는지 확인해보고 제거하기.
            this.displayDatetime = live.getStartDatetime().format(START_TIME_FORMAT).concat(" ~ ");
            this.displayDatetime = displayDatetime.concat(Optional.ofNullable(live.getEndDatetime())
                    .map(time -> time.format(END_TIME_FORMAT))
                    .orElse("무제한"));

            this.tutor = new UserResponse(live.getTutor());
            this.attendee = new AttendeeResponse(attendee);
            this.attendees = attendees.stream()
                    .map(item -> new AttendeeResponse(item))
                    .collect(Collectors.toList());
            this.enrolledCount = attendees.size();
            this.chime = new ChimeResponse(chime);
        }
    }
}
