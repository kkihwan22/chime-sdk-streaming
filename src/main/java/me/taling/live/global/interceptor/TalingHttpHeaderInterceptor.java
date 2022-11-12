package me.taling.live.global.interceptor;

import me.taling.live.core.domain.TalingHttpHeader;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.global.vo.ConnectDevice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TalingHttpHeaderInterceptor implements HandlerInterceptor {
    private final static Logger log = LoggerFactory.getLogger(TalingHttpHeaderInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("[interceptor] taling header.");
        log.debug("method:{}", request.getMethod());
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        ConnectDevice connectDevice = ConnectDevice.WEB;

        String userAgent = StringUtils.isNotBlank(request.getHeader("Taling-User-Agent")) ? StringUtils.upperCase(request.getHeader("Taling-User-Agent")) : "WEB";

        if (userAgent.equals("ANDROID")) {
            connectDevice = ConnectDevice.AOS;
        } else if (userAgent.equals("IOS")) {
            connectDevice = ConnectDevice.IOS;
        }

        ThreadLocalContextProvider.setTalingRequestHeader(TalingHttpHeader.builder()
                .connectDevice(connectDevice)
                .build());
        return true;
    }
}
