package me.taling.live.infra.feigns.space;

import feign.Response;
import feign.codec.ErrorDecoder;
import me.taling.live.global.exceptions.LiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.taling.live.global.exceptions.LiveException.ErrorCode.UNKNOWN_ERROR_MESSAGE;


public class SpaceErrorDecoder implements ErrorDecoder {
    private final static Logger log = LoggerFactory.getLogger(SpaceErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("method id: {}, response: {}", methodKey, response);
        return new LiveException(UNKNOWN_ERROR_MESSAGE);
    }
}