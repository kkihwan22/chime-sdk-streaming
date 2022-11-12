package me.taling.live.infra.feigns.auth;

import lombok.*;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.infra.feigns.auth.config.AuthFeignConfiguration;
import me.taling.live.infra.feigns.common.decoder.CommonErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "taling-auth",
        url = "${internal-endpoints.taling-web}",
        configuration = {
                AuthFeignConfiguration.class,
                CommonErrorDecoder.class
        }
)
public interface AuthClient {

    @PostMapping("/api3/account/accountAuthentication.php")
    SuccessResponseWrapper<Response> authentication();

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class Response {
        private Long id;
        private String nickname;
        private String profileImageUrl;
        private String profileThumbnailUrl;
    }
}
