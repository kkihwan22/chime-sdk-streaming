package me.taling.live.infra.aws.ecs;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EcsClient {
	private final AmazonECS ecs;

	public RunTaskResult runTask(RunTaskRequest request) {
		return ecs.runTask(request);
	}

	public StartTaskResult startTask(StartTaskRequest request) {
		return ecs.startTask(request);
	}

	public RegisterContainerInstanceResult registerContainerInstance(RegisterContainerInstanceRequest request) {
		return ecs.registerContainerInstance(request);
	}

	public StopTaskResult stopTaskResult(StopTaskRequest request) {
		return ecs.stopTask(request);
	}

	public DescribeTasksResult describeTasks(DescribeTasksRequest request) {
		return ecs.describeTasks(request);
	}

	public ListTaskDefinitionsResult listTaskDefinitions(ListTaskDefinitionsRequest request) {
		return ecs.listTaskDefinitions(request);
	}
}
