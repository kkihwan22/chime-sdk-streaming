package me.taling.live.api.asset;

import me.taling.live.api.asset.wrapper.ErrorResponseWrapper;
import me.taling.live.global.exceptions.BadParameterException;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @ExceptionHandler(BadParameterException.class)
    public ErrorResponseWrapper handleBadParameterException(BadParameterException e) {
        log.error("reason: {}", e.getReason());
        return new ErrorResponseWrapper(e.getError());
    }

    @ExceptionHandler(LiveException.class)
    public ErrorResponseWrapper handleCustomException(LiveException e) {
        log.error("reason: {}", e.getError().getReason());
        return new ErrorResponseWrapper(e.getError());
    }

    //todo 에러코드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponseWrapper httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("reason: {}", e.getMessage()); // Request method 'GET' not supported
        return new ErrorResponseWrapper(new ErrorResponseWrapper.Error("11", "400", "000", e.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseWrapper methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("reason: {}", e.getMessage());
        return new ErrorResponseWrapper(new ErrorResponseWrapper.Error("11", "400", "000", e.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponseWrapper httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("reason: {}", e.getMessage()); // Required request body is missing
        return new ErrorResponseWrapper(new ErrorResponseWrapper.Error("11", "400", "000", e.getLocalizedMessage()));
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ErrorResponseWrapper httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("reason: {}", e.getMessage()); // Content type '~' not supported
        return new ErrorResponseWrapper(new ErrorResponseWrapper.Error("11", "400", "000", e.getLocalizedMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseWrapper handleException(Exception e, WebRequest request) {
        log.error("Unknown Exception: {}", e);
        log.error("Unknown Exception: {}", e.fillInStackTrace());
        log.error("Request:{}", request);
        return new ErrorResponseWrapper(new ErrorResponseWrapper.Error("11", "500", "000", "관리자에게 문의하세요."));
    }
}
