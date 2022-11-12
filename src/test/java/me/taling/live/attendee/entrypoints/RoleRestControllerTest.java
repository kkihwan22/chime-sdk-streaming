package me.taling.live.attendee.entrypoints;

import me.taling.live.attendee.applications.RoleUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RoleRestController.class)
@DisplayName("권한 부여 및 회수 요청 테스트")
class RoleRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RoleUsecase roleUsecase;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RoleRestController(roleUsecase))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .addInterceptors()
                .build();

    }

    @Test
    public void test_success_grant_manager() throws Exception {

        // given
        // given(roleUsecase.execute(any()));

        // when
        final ResultActions resultActions = mockMvc.perform(
                patch("/lives/1000/attendees/1000/role/MANAGER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
        // andReturn 공부
    }

}