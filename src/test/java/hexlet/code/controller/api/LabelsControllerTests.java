package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.ModelGenerator;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
        labelRepository.save(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/labels").with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("feature");
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGenerator.getLabelModel())
                .create();
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/labels")
                                .with(token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var label = labelRepository.findByName(data.getName()).get();
        assertNotNull(label);
        Assertions.assertThat(label.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/labels/{id}", testLabel.getId())
                        .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testLabel.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("name", "testName");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/labels/{id}", testLabel.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var label = labelRepository.findById(testLabel.getId()).get();
        Assertions.assertThat(label.getName()).isEqualTo("testName");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/labels/{id}", testLabel.getId()).with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }
}
