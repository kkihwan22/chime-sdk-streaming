package me.taling.live.api.asset.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.slf4j.MDC;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Getter
public class SuccessResponseWrapper<T> {
    @Schema(description = "응답결과", example = "success")
    private String result;

    @Schema(description = "응답세지")
    private String reason;

    @Schema(description = "응답시간(Asia/Seoul)")
    private LocalDateTime timestamp;

    @Schema(description = "요청에 대한 trace 식별번호")
    private String traceId;

    @Schema(description = "응답데이터")
    private T data;

    public SuccessResponseWrapper(T data) {
        this.result = "success";
        this.reason = "ok";
        this.timestamp = LocalDateTime.now();
        this.traceId = MDC.get("traceId");
        this.data = data;
    }

    public static <T> SuccessResponseWrapper success(T data) {
        SuccessResponseWrapper<T> response = new SuccessResponseWrapper();
        response.result = "success";
        response.reason = "ok";
        response.timestamp = LocalDateTime.now();
        response.traceId = MDC.get("traceId");
        response.data = data;
        return response;
    }
}