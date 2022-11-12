package me.taling.live.global.interceptor;

import me.taling.live.global.ThreadLocalContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadLocalContextInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(ThreadLocalContextInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("[interceptor] thread local context.");
        ThreadLocalContextProvider.remove();
        ThreadLocalContextProvider.removeTalingRequestHeader();
        return true;
    }
}
