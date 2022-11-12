package me.taling.live.infra.feigns.space;

import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "taling-message",
        url = "${internal-endpoints.taling-message}",
        configuration = {
                SpaceFeignConfiguration.class,
                SpaceErrorDecoder.class
        }
)
public interface SpaceFeignClient {
    @PostMapping("/v1/channels")
    SuccessResponseWrapper<SpaceFeignResponse> postChannel(@RequestBody SpaceFeignRequest request);

    @PutMapping("/v1/channels/{channelId}")
    SuccessResponseWrapper<SpaceFeignResponse> putChannel(@PathVariable String channelId, @RequestBody SpaceFeignRequest request);

    @GetMapping("/v1/channels/{channelId}")
    SuccessResponseWrapper<SpaceFeignResponse> getChannel(@PathVariable String channelId);

    @GetMapping("/v1/channels/{channelId}/users/{userId}")
    SuccessResponseWrapper<SpaceUserResponse> getUser(@PathVariable String channelId, @PathVariable Long userId);

    // TODO: feign 의존성 변경 후 >> @PatchMapping("/v1/channels/{channelId}/users/{userId}")
    @PutMapping("/v1/channels/{channelId}/users/{userId}/profile")
    SuccessResponseWrapper<SpaceUserResponse> patchUser(@PathVariable String channelId, @PathVariable Long userId, @RequestBody SpaceUserRequest request);
}