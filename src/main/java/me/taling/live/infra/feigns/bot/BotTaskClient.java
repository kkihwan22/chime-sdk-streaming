package me.taling.live.infra.feigns.bot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

@FeignClient(
	name = "taling-live-bot",
	url = "http://localhost:3000"
)
public interface BotTaskClient {
	@PostMapping("/record")
	BotTaskResponse streaming(URI baseUrl);

	@NoArgsConstructor
	@Getter
	class BotTaskResponse {
		private Long statusCode;
		private String message;
	}
}
