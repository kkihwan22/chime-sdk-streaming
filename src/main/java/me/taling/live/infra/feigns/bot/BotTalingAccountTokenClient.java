package me.taling.live.infra.feigns.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.infra.feigns.common.decoder.JacksonDecoderConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
		name = "taling-web-bot",
		url = "${internal-endpoints.taling-web}",
		configuration = {
				JacksonDecoderConfiguration.class
		}
)
public interface BotTalingAccountTokenClient {

	@PostMapping(value = "/api3/account/token.php")
	Response token(@RequestBody Request request);

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	class Response {
		private String accessToken;
		private Long expiresIn;
		private String tokenType;
		private String scope;
		private String refreshToken;
	}

	// todo 봇용 로그인토큰 관리 방법 논의 필요
	@Getter
	@NoArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	class Request {
		private static Request instance = new Request();
		private final String grantType = "password";
		private final String clientId = "taling";
		private final String clientSecret = "1234";
		private final String username = "livebot@taling.me";
		private final String password = "Ekfdldqht1!";

		public static Request of() {
			return instance;
		}
	}
}
