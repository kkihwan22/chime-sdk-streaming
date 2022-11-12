package me.taling.live.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Lives {
    private String liveId;
    private String title;
    private String channelId;
    private String meetingId;
    private LocalDateTime createdDatetime;
    private LocalDateTime endDatetime;

    public Lives(Live live) {
        this.liveId = live.getId();
        this.title = live.getTitle();
        this.channelId = live.getChannelId();
        this.meetingId = live.getMeetingId();
        this.createdDatetime = live.getCreateDatetime();
        this.endDatetime = live.getEndDatetime();
    }

    public Live toLive() {
        Live live = new Live(this.liveId).withMeetingId(meetingId);
        return live;
    }
}