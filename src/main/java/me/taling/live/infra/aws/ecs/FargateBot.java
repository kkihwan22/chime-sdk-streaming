package me.taling.live.infra.aws.ecs;

import com.amazonaws.services.ecs.model.*;
import lombok.RequiredArgsConstructor;
import me.taling.live.global.vo.BotEnvironmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class FargateBot implements BotAdapter {
	private static final String TASK_DEFINITION_FAMILY_PREFIX = "taling-live-bot-task";
	private static final String ECS_TASK_CONTAINER_NAME = "taling-live-bot-container";
	private static final int FIRST = 0;
	private static final int PORT_OF_BOT_INTERNAL_SERVER = 3000;

	@Value("${bot.ecs-cluster-arn}")
	private String ecsClusterArn;
	@Value("${bot.security-groups}")
	private String securityGroups;
	@Value("${bot.subnets}")
	private List<String> subnets;

	@Value("${redis.host}")
	private String redisHost;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final EcsClient client;

	public String join(String liveId, Map<BotEnvironmentKey, String> environmentVariables) {
		return client.runTask(makeTaskRequest(liveId, environmentVariables))
			.getTasks().get(FIRST)
			.getTaskArn();
	}

	public void stop(String taskId) {
		log.debug("[bot event] stop - taskId:[{}]", taskId);
		StopTaskRequest stopTaskRequest = new StopTaskRequest()
			.withCluster(ecsClusterArn)
			.withTask(taskId);

		client.stopTaskResult(stopTaskRequest);
	}

	private String getLatestTaskDefinitionArn() {
		// todo redis 캐싱 5분
		ListTaskDefinitionsResult listTask = client.listTaskDefinitions(
			new ListTaskDefinitionsRequest()
				.withFamilyPrefix(TASK_DEFINITION_FAMILY_PREFIX)
				.withSort(SortOrder.DESC));

		return listTask.getTaskDefinitionArns().get(FIRST);
	}

	private RunTaskRequest makeTaskRequest(String liveId, Map<BotEnvironmentKey, String> environmentVariables) {
		String groupName = "liveId:" + liveId;

		return new RunTaskRequest()
			.withCluster(ecsClusterArn)
			.withLaunchType(LaunchType.FARGATE)
			.withCount(1)
			.withGroup(groupName)
			.withTaskDefinition(getLatestTaskDefinitionArn())
			.withOverrides(new TaskOverride()
				.withContainerOverrides(new ContainerOverride()
					.withName(ECS_TASK_CONTAINER_NAME)
					.withEnvironment(
						new KeyValuePair().withName(BotEnvironmentKey.LIVE_URL.toString()).withValue(environmentVariables.get(BotEnvironmentKey.LIVE_URL))
						, new KeyValuePair().withName(BotEnvironmentKey.RTMP_URL.toString()).withValue(environmentVariables.get(BotEnvironmentKey.RTMP_URL))
						, new KeyValuePair().withName(BotEnvironmentKey.STREAM_KEY.toString()).withValue(environmentVariables.get(BotEnvironmentKey.STREAM_KEY))
						, new KeyValuePair().withName("PORT").withValue(String.valueOf(PORT_OF_BOT_INTERNAL_SERVER))
						, new KeyValuePair().withName("LIVE_ID").withValue(liveId)
						, new KeyValuePair().withName("REDIS").withValue(redisHost)
					)))
			.withNetworkConfiguration(new NetworkConfiguration()
				.withAwsvpcConfiguration(new AwsVpcConfiguration()
					.withAssignPublicIp(AssignPublicIp.ENABLED)
					.withSubnets(subnets)
					.withSecurityGroups(securityGroups)));
	}
}
