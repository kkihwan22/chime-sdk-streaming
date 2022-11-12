package me.taling.live.global.interceptor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "services")
@Component
public class ServicesConfigProperties {

    @Setter
    @Getter
    private Map<String, KeyMap> reference = new HashMap<>();

    @Setter
    @Getter
    public static class KeyMap {
        private String appKey;
        private String secretKey;
    }
}