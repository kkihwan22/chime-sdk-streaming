package me.taling.live.api;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @Value("${internal-endpoints.taling-web}")
    @Setter
    private String context;

    @Value("${internal-endpoints.taling-live-front}")
    @Setter
    private String front;

    @Value("${cookie-name}")
    @Setter
    private String cookieName;

    //@AdminAuthorization
    @RequestMapping(value = {"/", "/index.html"})
    public ModelAndView page() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("context", context);
        mav.addObject("front", front);
        mav.addObject("cookieName", cookieName);
        return mav;
    }

    @RequestMapping(value =  "/liveInfo/{liveId}")
    public ModelAndView liveInfoPage(@PathVariable String liveId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("liveInfo");
        mav.addObject("context", context);
        mav.addObject("front", front);
        mav.addObject("cookieName", cookieName);
        mav.addObject("liveId", liveId);
        return mav;
    }
}
