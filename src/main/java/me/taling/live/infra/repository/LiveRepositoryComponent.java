package me.taling.live.infra.repository;

import me.taling.live.core.domain.Live;

import java.util.List;

public interface LiveRepositoryComponent {
    void initLive(Live live);
    void reservedLive(Live live);
    List<Live> findLives();
    Live findLive(String liveId);
    List<Live> findLivesByStartDatetime(String startDatetime);
    void deleteLive(String liveId);
    Live findLiveByMeetingId(String meetingId);
}
