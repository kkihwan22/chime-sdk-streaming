package me.taling.live.core.usecase.bot;

import lombok.Getter;
import lombok.ToString;
import me.taling.live.core.domain.Chime;

public interface BotComponent {

	Chime join(BotJoinParameter parameter);

	@ToString
	@Getter
	class BotJoinParameter {
		private String liveId;
		public BotJoinParameter() {}

		public BotJoinParameter withLiveId(String liveId) {
			this.liveId = liveId;
			return this;
		}
	}

	@Getter
	class BotTaskInfo {
		private String taskId;
		BotTaskInfo() {}
	}
}
