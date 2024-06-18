package hexlet.code.app.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/welcome").with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Welcome to Spring");
    }
}
