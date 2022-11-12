package me.taling.live.global.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllowIpInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AllowIpInterceptor.class);
    private static final String[] WHITELIST_IP_ADDRESS = {"172.107.154.163"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("[interceptor] IP allow.");
        String ip = request.getHeader("X-FORWARDED-FOR");
        String remoteAddr = request.getRemoteAddr();
        log.info("ip: {}, remote ip :{}",ip, remoteAddr);

        for (String allowIp: WHITELIST_IP_ADDRESS) {
            if (ip.matches(allowIp)) {
                return true;
            }
        }
        log.error("허용되지 않은 IP 접근입니다. ");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
