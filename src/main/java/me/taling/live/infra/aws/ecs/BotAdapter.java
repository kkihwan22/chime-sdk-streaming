package me.taling.live.infra.aws.ecs;

import me.taling.live.global.vo.BotEnvironmentKey;

import java.util.Map;

public interface BotAdapter {
	String join(String liveId, Map<BotEnvironmentKey, String> environmentVariables);
	void stop(String taskId);
}
