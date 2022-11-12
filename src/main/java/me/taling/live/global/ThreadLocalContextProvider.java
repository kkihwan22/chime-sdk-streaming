package me.taling.live.global;

import me.taling.live.core.domain.TalingHttpHeader;
import me.taling.live.core.domain.User;

public class ThreadLocalContextProvider {
    private static ThreadLocal<User> globalUser = ThreadLocal.withInitial(() -> new User());
    private static ThreadLocal<TalingHttpHeader> talingHttpHeader = ThreadLocal.withInitial(() -> new TalingHttpHeader());

    //todo: 메소드 명 상세화 필요
    public static User get() {
        return globalUser.get();
    }

    public static void remove() {
        globalUser.remove();
    }

    public static void set(User owner) {
        globalUser.set(owner);
    }

    public static TalingHttpHeader getTalingRequestHeader() {
        return talingHttpHeader.get();
    }

    public static void removeTalingRequestHeader() {
        talingHttpHeader.remove();
    }

    public static void setTalingRequestHeader(TalingHttpHeader header) {
        talingHttpHeader.set(header);
    }
}