package me.taling.live.global.vo;

public enum BotEnvironmentKey {
	LIVE_URL,
	RTMP_URL,
	STREAM_KEY;

	@Override
	public String toString() {
		return super.toString().toUpperCase();
	}
}
