package me.taling.live.global.aspect;

import com.amazonaws.services.chime.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import me.taling.live.global.exceptions.LiveException;
import me.taling.live.infra.aws.chime.ChimeClientParameter;
import me.taling.live.listener.LiveListener.Event;
import me.taling.live.listener.LiveListener.Type;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.NOT_FOUND_CHIME_MEETING;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.UNKNOWN_ERROR_CHIME;

@RequiredArgsConstructor
@Component
@Aspect
public class ChimeAroundAspect {
    private final Logger log = LoggerFactory.getLogger(ChimeAroundAspect.class);
    private final ApplicationEventPublisher publisher;

    @Pointcut("execution(* me.taling.live.infra.aws.chime.ChimeClient.*(..))")
    public void aroundChimePointCut() {
    }

    @Around("aroundChimePointCut()")
    public Object print(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Signature:{}", joinPoint.getSignature().getName());

        ChimeClientParameter parameter = null;
        for (Object args : joinPoint.getArgs()) {
            log.debug("request: {}", args);
            if (args instanceof ChimeClientParameter) {
                parameter = (ChimeClientParameter) args;
            }
        }

        try {
            Object result = joinPoint.proceed();
            log.debug("result:{}", result);
            return result;
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                Event<List<String>> liveEvent = new Event<>(Type.EXPIRED_MEETING, Collections.singletonList(parameter.getLiveId()));
                log.debug("liveEvent:{}", liveEvent);
                publisher.publishEvent(liveEvent);
                throw new LiveException(NOT_FOUND_CHIME_MEETING);
            }
            log.error("[chime error]", e);
            // todo : 진짜 500으로 터져라.!
            throw new LiveException(UNKNOWN_ERROR_CHIME);
        }
    }
}
