package me.taling.live.api.internal;

import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.BaseRestController;
import me.taling.live.api.asset.response.DefaultResponse;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StreamRestController implements BaseRestController {
    private final Logger log = LoggerFactory.getLogger(StreamRestController.class);

    //private final CloudFrontManager cloudFrontManager;

    @GetMapping("/stream/signed-cookies")
    public SuccessResponseWrapper<DefaultResponse> getSignedCookies(HttpServletResponse response) {
        List<Cookie> cookies = new ArrayList<>();
        log.debug("cookies:{}", cookies);

        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }

        return SuccessResponseWrapper.success(new DefaultResponse("https://stream.dev.taling.me/ivs/v1/639374893179/3VhLasQVYMd1/2021/11/9/10/25/VrZ0EnXMeHEW/media/hls/master.m3u8"));
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
