package me.taling.live.api.attendee;

import me.taling.live.attendee.entrypoints.ActionRestController;
import me.taling.live.core.usecase.action.ActionComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
@DisplayName("사용자 액션 요청 테스트")
class ActionRestControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private ActionComponent actionComponent;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ActionRestController(actionComponent))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

    }
}