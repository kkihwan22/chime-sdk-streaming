package me.taling.live.api;

import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.internal.InternalRestController;
import me.taling.live.core.application.sequence.SequenceGenerator;
import me.taling.live.core.application.space.SpaceComponent;
import me.taling.live.core.usecase.bot.BotComponent;
import me.taling.live.core.usecase.query.QueryComponent;
import me.taling.live.infra.feigns.auth.AuthClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static me.taling.live.infra.feigns.auth.AuthClient.Response;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InternalRestController.class)
@DisplayName("EventRestController 테스트")
class InternalRestControllerTest {
    private final Logger log = LoggerFactory.getLogger(InternalRestControllerTest.class);

    private MockMvc mvc;

    @MockBean
    private BotComponent botComponent;

    @MockBean
    private QueryComponent queryComponent;

    @MockBean
    private SequenceGenerator sequenceGenerator;

    @MockBean
    private ApplicationEventPublisher publisher;

    @MockBean
    private SpaceComponent spaceComponent;


    @MockBean
    private AuthClient authClient;


//    @BeforeEach
//    public void init() {
//        mvc = MockMvcBuilders.standaloneSetup(new InternalRestController(botComponent, queryComponent, sequenceGenerator, publisher, spaceComponent))
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }


    //@Test
    void 테스트성공_handleChimeEvents() throws Exception {

        given(authClient.authentication())
                .willReturn(SuccessResponseWrapper.success(new Response(1L, "레구리", "", "")));

        String body = "{" +
                "\"version\":\"0\"," +
                "\"source\":\"aws.chime\"," +
                "\"account\":\"111122223333\"," +
                "\"id\":\"12345678-1234-1234-1234-111122223333\"," +
                "\"region\":\"us-east-1\"," +
                "\"detail-type\":\"ChimeMeetingStateChange\"," +
                "\"time\":\"2021-10-19T17:00:00Z\"," +
                "\"resources\":[]," +
                "\"detail\":{" +
                "\"version\":\"0\"," +
                "\"eventType\":\"chime:AttendeeDeleted\"," +
                "\"timestamp\":12344566754," +
                "\"meetingId\":\"87654321-4321-4321-1234-111122223333\"," +
                "\"attendeeId\":\"87654321-4321-4321-1234-111122223333\"," +
                "\"externalUserId\":\"87654321-4321-4321-1234-111122223333\"," +
                "\"mediaRegion\":\"us-east-1\"" +
                "}" +
                "}";

        mvc.perform(
                        put("/internals/events/chime")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
        ;
    }
}