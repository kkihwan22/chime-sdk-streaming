package me.taling.live.global.configurations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
@Configuration
public class EmbeddedConfiguration {
    private final Logger log = LoggerFactory.getLogger(EmbeddedConfiguration.class);

    private RedisServer redisServer;

    @PostConstruct
    public void init() throws Exception {

        redisServer = new RedisServer(6378);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }


}
