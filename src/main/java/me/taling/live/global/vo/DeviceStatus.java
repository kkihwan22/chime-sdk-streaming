package me.taling.live.global.vo;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DeviceStatus {
    ON, OFF, LOCK
    ;

    private static final Map<String, DeviceStatus> map
            = Arrays.stream(DeviceStatus.values()).collect(Collectors.toMap(DeviceStatus::name, Function.identity()));

    public static DeviceStatus from(String key) {
        return Optional.ofNullable(map.get(key)).orElseThrow(() -> new IllegalArgumentException());
    }

}
