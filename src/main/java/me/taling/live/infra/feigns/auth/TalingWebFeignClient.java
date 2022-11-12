package me.taling.live.infra.feigns.auth;

import lombok.*;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.infra.feigns.common.decoder.CommonErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "taling-web",
        url = "${internal-endpoints.taling-web}",
        configuration = {
                CommonErrorDecoder.class
        }
)
public interface TalingWebFeignClient {
    @GetMapping("/api3/live/startLiveClass.php")
    SuccessResponseWrapper<Response> startLive(@RequestParam String liveId,
                                               @RequestParam String previewUrl,
                                               @RequestParam String replayUrl,
                                               @RequestParam String replayOriginalUrl);

    @GetMapping("/api3/live/endLiveClass.php?replayRunningTime=0&replayFileSize=0")
    SuccessResponseWrapper<Response> closeLive(@RequestParam String liveId);

    @GetMapping("/api3/live/updateLiveClass.php")
    SuccessResponseWrapper<Response> updateLive(@RequestParam String liveId,
                                                @RequestParam String replayFileSize,
                                                @RequestParam String replayRunningTime,
                                                @RequestParam String replayOriginFileUrl,
                                                @RequestParam String replayUrl);


    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class Response {
        private String id;
    }
}
