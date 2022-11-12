package me.taling.live.infra.feigns.space;

import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpaceFeignConfiguration {
    private final static Logger log = LoggerFactory.getLogger(SpaceFeignConfiguration.class);

    @Bean
    public RequestInterceptor messageHeaderInterceptor() {
        return requestTemplate -> {

            // todo : 환경변수로 분리.
            Map<String, Collection<String>> headers = new HashMap<>();
            headers.put("client-id", Collections.singleton("FWDNuKfsx_K161PBfwWC27oHspce55bwWUMrwPKbDRFV2oCMIx"));
            headers.put("client-secret", Collections.singleton("z6d1Hh0zONgeb1HHZ8KULr47oBc8S95QGNuk-Q5gdxtpIbXa_n"));
            requestTemplate.headers(headers);
        };
    }

    @Bean
    public SpaceErrorDecoder messagingErrorDecoder() {
        return new SpaceErrorDecoder();
    }
}