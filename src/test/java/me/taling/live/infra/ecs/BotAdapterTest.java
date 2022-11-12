package me.taling.live.infra.ecs;

import me.taling.live.Application;
import me.taling.live.global.vo.BotEnvironmentKey;
import me.taling.live.infra.aws.ecs.BotAdapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class BotAdapterTest {
	private final static String liveCamUrl = "https://taling.me";
	private final static String rtmpUrl = "rtmps://ae2301763bb7.global-contribute.live-video.net:443/app/";
	private final static String streamKey = "sk_us-east-1_pVCdQxCMCV1L_GyUNwdNItqG9llFOf8lyExbw64GZBA";

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final BotAdapter botAdapter;

	@Autowired
	public BotAdapterTest(BotAdapter botAdapter) {
		this.botAdapter = botAdapter;
	}

	@Test
	void joinAndStop() {
		Map<BotEnvironmentKey, String> environmentVariables = new EnumMap<>(BotEnvironmentKey.class);
		environmentVariables.put(BotEnvironmentKey.LIVE_URL, liveCamUrl);
		environmentVariables.put(BotEnvironmentKey.RTMP_URL, rtmpUrl);
		environmentVariables.put(BotEnvironmentKey.STREAM_KEY, streamKey);

		String taskId = botAdapter.join("test", environmentVariables);
		log.debug(taskId);
		botAdapter.stop(taskId);

		assertThat(taskId).isNotEmpty();
	}
}
