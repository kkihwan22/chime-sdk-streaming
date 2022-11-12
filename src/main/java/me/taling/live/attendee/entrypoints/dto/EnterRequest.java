package me.taling.live.attendee.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.global.exceptions.BadParameterException;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class EnterRequest {
    private String video;
    private String audio;

    public void valid() {
        assertThrow(video);
        assertThrow(audio);
    }

    private void assertThrow(String device) {
        if (StringUtils.isNotBlank(device) && !device.matches("ON|OFF|LOCK|on|off|lock")) {
            throw new BadParameterException("Only on or off value is allowed.");
        }
    }
}
