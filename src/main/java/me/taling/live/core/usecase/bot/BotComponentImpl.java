package me.taling.live.core.usecase.bot;

import lombok.RequiredArgsConstructor;
import me.taling.live.core.domain.Chime;
import me.taling.live.core.domain.Live;
import me.taling.live.infra.aws.chime.ChimeClient;
import me.taling.live.infra.repository.LiveRepositoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BotComponentImpl implements BotComponent {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final LiveRepositoryComponent liveRepositoryComponent;
	private final ChimeClient chimeClient;

	@Override
	public Chime join(BotJoinParameter parameter) {
		if (parameter.getLiveId() == null || parameter.getLiveId().isBlank()) {
			throw new IllegalArgumentException("Not Valid : liveId");
		}

		Live live = liveRepositoryComponent.findLive(parameter.getLiveId());
		log.debug("live:{}", live);

		Chime chime = chimeClient.attendBot(live);
		live.withChime(chime);
		return chime;
	}
}
