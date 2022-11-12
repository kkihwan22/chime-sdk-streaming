package me.taling.live.global.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.taling.live.api.asset.wrapper.ErrorResponseWrapper;


@EqualsAndHashCode(callSuper = false)
@ToString
public class BadParameterException extends RuntimeException {
    private final static String CODE = "400";
    private final static String SUBCODE = "000";

    @Getter
    private final String reason;

    public BadParameterException(String reason) {
        this.reason = reason;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public ErrorResponseWrapper.Error getError() {
        return new ErrorResponseWrapper.Error("11", this.CODE, this.SUBCODE, this.reason);
    }
}