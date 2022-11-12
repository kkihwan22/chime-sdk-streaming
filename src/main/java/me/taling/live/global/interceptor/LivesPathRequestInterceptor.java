package me.taling.live.global.interceptor;


import me.taling.live.core.domain.User;
import me.taling.live.global.ThreadLocalContextProvider;
import me.taling.live.infra.feigns.auth.AuthClient;
import me.taling.live.infra.feigns.auth.AuthClient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LivesPathRequestInterceptor implements HandlerInterceptor {
    private final static Logger log = LoggerFactory.getLogger(LivesPathRequestInterceptor.class);

    @Autowired
    private AuthClient authClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("[interceptor] Live path.");
        log.debug("method:{}", request.getMethod());
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        Response authenticatedUser = authClient.authentication().getData();
        ThreadLocalContextProvider.set(User.builder()
            .id(authenticatedUser.getId())
            .nickname(authenticatedUser.getNickname())
            .imageUrl(authenticatedUser.getProfileImageUrl())
            .thumbnailUrl(authenticatedUser.getProfileThumbnailUrl())
            .connectDevice(ThreadLocalContextProvider.getTalingRequestHeader().getConnectDevice())
            .build());
        return true;
    }
}
