package me.taling.live.api.attendee.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class UpdateMyDeviceRequest {

    @NotEmpty
    @Pattern(regexp = "VIDEO|AUDIO|SHARE")
    private String deviceType;

    @NotEmpty
    @Pattern(regexp = "ON|OFF")
    private String deviceStatus;
}
