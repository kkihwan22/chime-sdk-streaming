package me.taling.live.infra.feigns.auth.config;

import feign.RequestInterceptor;
import me.taling.live.infra.feigns.common.decoder.CommonErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class AuthFeignConfiguration {
    private final static Logger log = LoggerFactory.getLogger(AuthFeignConfiguration.class);

    @Bean
    public RequestInterceptor authHeaderInterceptor() {
        return requestTemplate -> {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();

            Map<String, Collection<String>> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                if (key.equals("content-length")) {
                    log.debug("skip content-length.");
                    continue;
                }
                headers.put(key, Collections.list(request.getHeaders(key)));
            }
            log.debug("headers:{}", headers);
            requestTemplate.headers(headers);
        };
    }

    @Bean
    public CommonErrorDecoder talingWebErrorDecoder() {
        return new CommonErrorDecoder();
    }


}
