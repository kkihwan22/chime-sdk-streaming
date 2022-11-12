package me.taling.live.api.internal;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.BaseRestController;
import me.taling.live.api.asset.response.DefaultResponse;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.attendee.response.ChimeResponse;
import me.taling.live.api.internal.request.*;
import me.taling.live.api.internal.response.BotJoinResponse;
import me.taling.live.api.internal.response.IvsResponse;
import me.taling.live.api.internal.response.SpaceCreateResponse;
import me.taling.live.core.application.sequence.SequenceGenerator;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceParameter;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Ivs;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.Space;
import me.taling.live.core.usecase.attendee.UpdateAttendeesComponent;
import me.taling.live.core.usecase.attendee.UpdateAttendeesComponent.UpdateAttendeesParameter;
import me.taling.live.core.usecase.bot.BotComponent;
import me.taling.live.core.usecase.bot.BotComponent.BotJoinParameter;
import me.taling.live.core.usecase.live.*;
import me.taling.live.core.usecase.live.AmazonChimeEventTriggerComponent.AmazonChimeEventParameter;
import me.taling.live.core.usecase.live.IvsEventTriggerComponent.IvsEventParameter;
import me.taling.live.core.usecase.live.LessonLiveCreateComponent.LessonLiveCreateParameter;
import me.taling.live.core.usecase.live.LessonLiveCreateComponent.LessonLiveCreateResult;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.listener.LiveListener.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static me.taling.live.core.usecase.attendee.UpdateAttendeesComponent.UpdateAttendeesResult;
import static me.taling.live.listener.LiveListener.Type.CREATE_RESERVED_LIVE;

@RequiredArgsConstructor
@RestController
public class InternalRestController implements BaseRestController {
    private final static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private final Logger log = LoggerFactory.getLogger(InternalRestController.class);
    private final BotComponent botComponent;
    private final QueryComponent queryComponent;
    private final SequenceGenerator sequenceGenerator;
    private final ApplicationEventPublisher publisher;
    private final LessonLiveCreateComponent lessonLiveCreateComponent;
    private final UpdateAttendeesComponent updateAttendeesComponent;
    private final SpaceComponent spaceComponent;
    private final LiveClearComponent liveClearComponent;
    private final AmazonChimeEventTriggerComponent amazonChimeEventTriggerComponent;
    private final LiveForceCloseComponent liveForceCloseComponent;
    private final IvsEventTriggerComponent ivsEventTriggerComponent;
    private final IvsInfoComponent ivsInfoComponent;

    @ApiOperation(value = "Chime Event 처리", notes = "Aws chime event trigger 처리 (Amazon EventBrige 에서 호출")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @PostMapping("/internals/aws/events/chime")
    public String handleAwsEventChime(
            @RequestBody ChimeEventRequest request) {
        log.debug("request:{}", request);
        amazonChimeEventTriggerComponent.execute(
                new AmazonChimeEventParameter(request.getEventType(), request.getMeetingId(), request.getAttendeeId()));
        return "OK";
    }

    @ApiOperation(value = "미팅생성(예약)", notes = "예약 된 미팅을 생성한다. (Amazon EventBrige에서 호출)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @PostMapping("/internals/aws/events/lives/reservation")
    public String handleAwsEventLiveReservation() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("run cron : {}", now);
        Event<String> liveEvent = new Event<>(CREATE_RESERVED_LIVE, now.format(DATETIME_FORMATTER));
        log.debug("liveEvent:{}", liveEvent);
        publisher.publishEvent(liveEvent);
        return "OK";
    }

    @ApiOperation(value = "IVS 이벤트 처리", notes = "IVS recording 종료 시 S3 정보 전달")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @PostMapping("/internals/aws/events/ivs")
    public String handleAwsEventIvs(
            @RequestBody IvsEventRequest request) {
        log.debug("request:{}", request);
        ivsEventTriggerComponent.execute(new IvsEventParameter(
                request.getChannelName(),
                request.getStreamId(),
                request.getRecordingStatus(),
                request.getRecordingStatusReason(),
                request.getBucketName(),
                request.getKeyPrefix()));
        return "OK";
    }

    @ApiOperation(value = "Bot 조인", notes = "chime 생성 시, 봇을 조인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @PutMapping("/internals/{liveId}/attendees/bot")
    public SuccessResponseWrapper<BotJoinResponse> putBot(
            @PathVariable String liveId) {
        Live live = queryComponent.live(liveId);
        Chime result = botComponent.join(new BotJoinParameter().withLiveId(liveId));

        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new BotJoinResponse(live.getChannelId(), new ChimeResponse(result)));
    }

    @ApiOperation(value = "미팅생성(예약)", notes = "예약 된 미팅을 생성한다. (Amazon EventBrige에서 호출)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @GetMapping("/internals/commons/sequence")
    public SuccessResponseWrapper<List<String>> getSequence(
            @RequestParam(required = false, defaultValue = "1") Integer creationCount) {
        log.debug("creation count:{}", creationCount);

        List<String> sequences = sequenceGenerator.generate(creationCount);
        return SuccessResponseWrapper.success(sequences);
    }

    @PostMapping("/internals/space")
    public SuccessResponseWrapper<SpaceCreateResponse> postSpace(
            @RequestBody @Valid InternalSpaceRequest request, BindingResult bindingResult) {
        log.debug("request: {}", request);
        if (bindingResult.hasErrors()) {
            handleError(bindingResult.getAllErrors());
        }

        // instance 형식 라이브 생성 시 스페이스 생성
        if (request.getReferenceType().equals("MEETING") && request.getStartedAt() == null) {
            Space space = spaceComponent.post(new SpaceParameter(
                    request.getReferenceId(),
                    request.getReferenceType(),
                    request.getTitle(),
                    ThreadLocalContextProvider.get(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Boolean.TRUE
            ));
            log.debug("space: {}", space);
            return SuccessResponseWrapper.success(new SpaceCreateResponse(space));
        }

        Space space = spaceComponent.post(new SpaceParameter(
                request.getReferenceId(),
                request.getReferenceType(),
                request.getTitle(),
                request.getTutor(),
                request.getManagers(),
                request.getUsers(),
                Boolean.FALSE,
                request.getStartedAt(),
                request.getExpiredAt())
        );
        log.debug("space: {}", space);
        return SuccessResponseWrapper.success(new SpaceCreateResponse(space));
    }

    @PutMapping("/internals/space/{spaceId}")
    public SuccessResponseWrapper<SpaceCreateResponse> putSpace(
            @PathVariable String spaceId,
            @RequestBody @Valid InternalSpaceRequest request, BindingResult bindingResult) {
        log.debug("request: {}", request);
        if (bindingResult.hasErrors()) {
            handleError(bindingResult.getAllErrors());
        }

        Space space = spaceComponent.put(spaceId, new SpaceParameter(
                request.getReferenceId(),
                request.getReferenceType(),
                request.getTitle(),
                request.getTutor(),
                request.getManagers(),
                request.getUsers(),
                Boolean.FALSE,
                request.getStartedAt(),
                request.getExpiredAt())
        );

        log.debug("space: {}", space);
        return SuccessResponseWrapper.success(new SpaceCreateResponse(space));
    }

    @PostMapping("/internals/lives")
    public SuccessResponseWrapper<DefaultResponse> createLive(@RequestBody @Valid InternalLiveCreateRequest request, BindingResult bindingResult) {
        log.debug("request: {}", request);
        if (bindingResult.hasErrors()) {
            handleError(bindingResult.getAllErrors());
        }

        LessonLiveCreateResult result = lessonLiveCreateComponent.execute(new LessonLiveCreateParameter(
                request.getLiveId(),
                request.getChannelId(),
                request.getTitle(),
                request.getLiveMethod(),
                request.getRecCondition(),
                request.getStartedAt(),
                request.getExpiredAt()
        ));

        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new DefaultResponse(request.getLiveId()));
    }

    @PutMapping("/internals/live/{liveId}/users")
    public SuccessResponseWrapper<DefaultResponse> updateUsers(@PathVariable String liveId,
                                                               @RequestBody @Valid InternalRestUserUpdateRequest request, BindingResult bindingResult) {
        log.debug("request:{}", request);
        if (bindingResult.hasErrors()) {
            handleError(bindingResult.getAllErrors());
        }

        Live live = queryComponent.live(liveId);
        log.debug("find live:{}", live);

        UpdateAttendeesResult result = updateAttendeesComponent.execute(new UpdateAttendeesParameter(live, request.getAddUsers(), request.getRemoveUser()));
        log.debug("result:{}", result);

        return SuccessResponseWrapper.success(new DefaultResponse(result.getLiveId()));
    }

    // TODO: 개선 시 추가되어야 하는 API
    @DeleteMapping("/internals/lives/clear")
    public String clearLives() {
        liveClearComponent.execute();
        return "OK";
    }

    // TODO: 개선 시 추가되어야 하는 API
    @PatchMapping("/internals/lives/{liveId}/close")
    public String forceClose(@PathVariable String liveId) {
        liveForceCloseComponent.execute(liveId);
        return "OK";
    }

    @GetMapping("/internals/lives/{liveId}/ivsInfo")
    public SuccessResponseWrapper<IvsResponse> getIvsInfo(@PathVariable String liveId) {
        Ivs result = ivsInfoComponent.ivs(liveId);
        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new IvsResponse(result));
    }
}