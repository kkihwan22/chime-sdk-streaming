package me.taling.live.attendee.applications;

import me.taling.live.attendee.domain.AttendeeDataprovider;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.application.space.SpaceComponentImpl;
import me.taling.live.core.domain.Live;
import me.taling.live.core.domain.User;
import me.taling.live.core.usecase.tutor.TutorComponent;
import me.taling.live.core.usecase.tutor.TutorComponentImpl;
import me.taling.live.global.vo.DeviceStatus;
import me.taling.live.infra.redis.AttendeeRedisDataproviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static me.taling.live.attendee.applications.EnterLiveUsecase.EnterLiveParameter;

class EnterLiveServiceTest {

    private EnterLiveUsecase service;
    private AttendeeDataprovider attendeeDataprovider = Mockito.mock(AttendeeRedisDataproviderImpl.class);
    private SpaceComponent spaceComponent = Mockito.mock(SpaceComponentImpl.class);
    private AttendChimeComponent attendChimeComponent = Mockito.mock(AttendChimeComponentImpl.class);
    private TutorComponent tutorComponent = Mockito.mock(TutorComponentImpl.class);


    @BeforeEach
    void setup() {
        service = new EnterLiveService(attendeeDataprovider, spaceComponent, attendChimeComponent, tutorComponent);
    }

    @Test
    void test_actions() {
        EnterLiveParameter parameter = new EnterLiveParameter(
                new Live("1000"),
                User.builder().id(1000L).build(),
                DeviceStatus.OFF,
                DeviceStatus.OFF);

        service.execute(parameter);
    }
}