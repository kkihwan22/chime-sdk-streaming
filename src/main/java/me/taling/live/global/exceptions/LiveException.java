package me.taling.live.global.exceptions;

import lombok.Getter;
import me.taling.live.api.asset.wrapper.ErrorResponseWrapper;

public class LiveException extends RuntimeException {
    protected final static String SERVICE = "11";

    @Getter
    protected ErrorCode code;

    public LiveException(ErrorCode code) {
        this.code = code;
    }

    public ErrorResponseWrapper.Error getError() {
        return new ErrorResponseWrapper.Error("11", code.getCode(), code.subcode, code.getReason());
    }

    public boolean equalsErrorCode(ErrorCode errorCode) {
        return this.code == errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public enum ErrorCode {
        NOT_MATCHED_TUTOR("400", "100", "Not tutor."),
        UN_AUTHORIZED("401", "000", "Unauthorized error."),
        UN_ACCEPTABLE_USER("401", "000", "Unacceptable user."),
        NO_PERMISSION("403", "000", "Not have permission to request."),
        FORBIDDEN_DEVICE_LOCKED("403", "101", "The device was locked by the administrator."),
        NOT_FOUND_LIVE("404", "001", "Not found live(ByID)."),
        NOT_FOUND_USER("404", "002", "Not found user(ByID)."),
        NOT_FOUND_CHIME_MEETING("404", "003", "Not found aws chime meeting."),
        NOT_FOUND_CHIME_ATTENDEE("404", "004", "Not found aws chime attendee."),
        NOT_FOUND_ATTENDEE("404", "006", "Not found attendeeId"),
        NOT_FOUND_TALENT("404", "005", "Not found taling talents."),
        NOT_FOUND_CHANNEL_BY_IVS("404", "007", "Not found channel name of IVS"),
        CONFLICT("409", "000", "Conflict"),
        ALREADY_LEFT_ATTENDEE("409", "001", "Already left attendee."),
        NOT_RUNNING_BOT("409", "001", "Not running bot. try again."),
        UNKNOWN_ERROR_LIVE("500", "100", "[LIVE] Unknown errors have occurred."),
        UNKNOWN_ERROR_TALING("500", "101", "[TALING-WEB] Unknown errors have occurred."),
        UNKNOWN_ERROR_MESSAGE("500", "102", "[MESSAGE] Unknown errors have occurred."),
        UNKNOWN_ERROR_CHIME("500", "103", "[AWS-CHIME] Unknown errors have occurred."),
        UNKNOWN_ERROR_ECS("500", "104", "[AWS-ECS] Unknown errors have occurred."),
        ;


        @Getter
        private String code;
        @Getter
        private String subcode;
        @Getter
        private String reason;

        ErrorCode(String code, String subcode, String reason) {
            this.code = code;
            this.subcode = subcode;
            this.reason = reason;
        }

    }


}
