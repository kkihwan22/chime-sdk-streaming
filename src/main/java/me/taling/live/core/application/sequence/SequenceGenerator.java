package me.taling.live.core.application.sequence;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class SequenceGenerator {
    private final Logger log = LoggerFactory.getLogger(SequenceGenerator.class);
    private final ValueOperations valueOperations;
    private final String sequenceName = "live-sequence";
    private final String pattern = "yyMMddHHmmss";

    public SequenceGenerator(RedisTemplate redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    @PostConstruct
    public void init() {
        if (valueOperations.getOperations().hasKey(this.sequenceName) == false) {
            log.debug("Live sequence count zero.");
            valueOperations.set(this.sequenceName, 0);
        }
    }

    public String generate() {
        Long sequence = valueOperations.increment(this.sequenceName);
        log.debug("sequence:{}", sequence);

        String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern(this.pattern));
        log.debug("prefix:{}", prefix);
        if (sequence > 999) {
            valueOperations.set(this.sequenceName, 0);
        }

        String uniqueKey = prefix.concat(StringUtils.leftPad(String.valueOf(sequence), 3, "0"));
        log.debug("unique id:{}", uniqueKey);
        return uniqueKey;
    }

    public List<String> generate(final Integer creationCount) {
        List<String> sequences = new ArrayList<>();
        for (int i = 0; i < creationCount; i++) {
            sequences.add(this.generate());
        }
        log.debug("sequences: {}", sequences);
        return sequences;
    }
}