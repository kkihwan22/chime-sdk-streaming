package me.taling.live.api.asset.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Getter
public class ErrorResponseWrapper {
    @Schema(description = "응답결과", example = "failure")
    private String result;

    @Schema(description = "응답메세지")
    private String reason;

    @Schema(description = "응답시간(Asia/Seoul)")
    private LocalDateTime timestamp;

    @Schema(description = "요청에 대한 trace 식별번호")
    private String traceId;

    @Schema(description = "Error 정보")
    private Error error;

    public ErrorResponseWrapper(Error error) {
        this.result = "failure";
        this.timestamp = LocalDateTime.now();
        this.traceId = MDC.get("traceId");
        this.reason = "error.";
        this.error = error;
    }


    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    static public class Error {
        @Schema(description = "Error 발생 서비스")
        private String service;
        @Schema(description = "Error 코드")
        private String code;
        @Schema(description = "Error 상세코드")
        private String subcode;
        @Schema(description = "Error 내용(메세지)")
        private String reason;
    }
}