package me.taling.live.api.attendee.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.global.exceptions.BadParameterException;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class EntranceRequest {
    private String video;
    private String audio;

    public void valid() {
        assertThrow(video);
        assertThrow(audio);
    }

    private void assertThrow(String device) {
        if (StringUtils.isNotBlank(device) && !device.matches("ON|OFF")) {
            throw new BadParameterException("Only on or off value is allowed.");
        }
    }
}