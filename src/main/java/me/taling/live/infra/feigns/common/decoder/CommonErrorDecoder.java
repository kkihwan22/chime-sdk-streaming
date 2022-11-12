package me.taling.live.infra.feigns.common.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.UNKNOWN_ERROR_TALING;
import static me.taling.live.global.exceptions.LiveException.ErrorCode.UN_AUTHORIZED;

public class CommonErrorDecoder implements ErrorDecoder {
    private final static Logger log = LoggerFactory.getLogger(CommonErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("method id: {}, response: {}", methodKey, response);

        HttpStatus httpStatus = HttpStatus.resolve(response.status());

        if (httpStatus == HttpStatus.UNAUTHORIZED) {
            log.error("[FeignClient] An authentication error has occurred.");
            throw new LiveException(UN_AUTHORIZED);
        }

        return new LiveException(UNKNOWN_ERROR_TALING);
    }
}