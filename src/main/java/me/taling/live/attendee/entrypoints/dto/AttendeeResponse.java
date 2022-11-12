package me.taling.live.attendee.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.taling.live.attendee.domain.Attendee;
import me.taling.live.attendee.domain.AttendeeType;
import me.taling.live.global.vo.ConnectDevice;
import me.taling.live.global.vo.DeviceStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class AttendeeResponse {

    @Schema(description = "사용자 식별번호")
    private Long userId;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;

    @Schema(description = "프로필 썸네일 URL")
    private String profileThumbnailUrl;

    @Schema(description = "Chime Attendee 정보")
    private String attendeeId;

    @Schema(description = "참석자 타입 (튜터,튜티,조교,연사)")
    private AttendeeType attendeeType;

    @Schema(description = "참석자 디바이스 정보")
    private ConnectDevice connectDevice;

    @Schema(description = "입장시간 (UTC +9)")
    private LocalDateTime attendedDatetime;

    @Schema(description = "비디오상태")
    private DeviceStatus videoStatus;

    @Schema(description = "마이크상태")
    private DeviceStatus micStatus;

    @Schema(description = "손들기상태")
    private DeviceStatus handsStatus;

    @Schema(description = "화면공유상태")
    private DeviceStatus screenStatus;

    public AttendeeResponse(Attendee attendee) {
        this.userId = attendee.getUserId();
        this.attendeeType = attendee.getAttendeeType();
        this.connectDevice = attendee.getConnectDevice();
        this.nickname = attendee.getNickname();
        this.profileImageUrl = attendee.getImageUrl();
        this.profileThumbnailUrl = attendee.getThumbnailUrl();
        this.attendedDatetime = attendee.getCreatedDatetime();
        this.attendeeId = attendee.getAttendeeId();
        this.videoStatus = attendee.getVideoStatus();
        this.micStatus = attendee.getMicStatus();
        this.handsStatus = attendee.getHandsStatus();
        this.screenStatus = attendee.getScreenStatus();
    }
}