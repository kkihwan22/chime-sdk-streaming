package me.taling.live.global.aspect;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.application.sequence.SequenceGenerator;
import me.taling.live.global.aspect.annotations.UniqueKey;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static me.taling.live.core.usecase.live.CreateComponent.CreateParameter;

//TODO: poincut 에 대해 annotation 적용
//TODO: parameter에 대해서 interface 활용

/**
 * 사용하지 않음. 나중에 개선을 후 적용 예정.!
 */
@RequiredArgsConstructor
@Component
@Aspect
public class UniqueKeyAspect {
    private static final Logger log = LoggerFactory.getLogger(UniqueKeyAspect.class);
    private final SequenceGenerator sequenceGenerator;

    //@Pointcut("execution(* me.taling.live.core.component.create.ClassroomCreateComponent.*(..))")
    public void create() {
    }

    //@Before("create()")
    public void before(JoinPoint point) throws IllegalAccessException {
        log.debug("start created UniqueKey");
        for (Object c : point.getArgs()) {
            if (c instanceof CreateParameter) {
                Field field = hasUniqueKeyAnnotation(c.getClass().getDeclaredFields());
                if (field != null) {
                    field.setAccessible(true);
                    String uk = sequenceGenerator.generate();
                    log.debug("Unique id:{}", uk);
                    field.set(c, uk);
                }
                break;
            }
        }
        log.debug("end created UniqueKey");
    }

    private Field hasUniqueKeyAnnotation(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(UniqueKey.class)) {
                return field;
            }
        }
        return null;
    }
}