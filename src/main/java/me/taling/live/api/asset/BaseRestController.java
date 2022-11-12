package me.taling.live.api.asset;

import me.taling.live.global.exceptions.BadParameterException;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseRestController {

    default void handleError(List<ObjectError> errors) {
        String reason = errors
                .stream()
                .map(err -> err.getObjectName() + ":" + err.getDefaultMessage())
                .collect(Collectors.joining(","));
        throw new BadParameterException(reason);
    }
}