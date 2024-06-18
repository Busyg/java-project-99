package hexlet.code.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testTask = Instancio.of(modelGenerator.getTaskModel())
                .create();
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks").with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testTask.getDescription());
    }

    @Test
    public void testIndexTitleFilter() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/tasks?titleCont=" + testTask.getName()
        ).with(jwt())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testTask.getName());
    }

    @Test
    public void testIndexAssigneeFilter() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/tasks?assigneeId=" + testTask.getAssignee().getId()
        ).with(jwt())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testTask.getAssignee().getId().toString());
    }

    @Test
    public void testIndexStatusFilter() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/tasks?status=" + testTask.getTaskStatus().getSlug()
        ).with(jwt())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testTask.getTaskStatus().getSlug());
    }

    /*@Test
    public void testIndexLabelFilter() throws Exception {
        var labelId = testTask.getLabels().stream().findFirst().get().getId();
        var result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/tasks?labelId=" + labelId
        ).with(jwt())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(labelId.toString());
    }*/

    @Test
    @Transactional
    public void testCreate() throws Exception {
        var data = new TaskCreateDTO();
        data.setIndex(9000);
        data.setAssigneeId(1L);
        data.setTitle("Task 1");
        data.setContent("Description of task 1");
        data.setStatus("to_be_fixed");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tasks")
                                .with(token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
        var task = taskRepository.findByName(data.getTitle()).get();
        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(data.getTitle());
        assertThat(task.getIndex()).isEqualTo(data.getIndex());
        assertThat(task.getDescription()).isEqualTo(data.getContent());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
        assertThat(task.getAssignee().getId()).isEqualTo(data.getAssigneeId());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", testTask.getId())
                        .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(testTask.getDescription());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new TaskUpdateDTO();
        data.setIndex(JsonNullable.of(9000));
        data.setAssigneeId(JsonNullable.of(1L));
        data.setTitle(JsonNullable.of("Task 1"));
        data.setContent(JsonNullable.of("Description of task 1"));
        data.setStatus(JsonNullable.of("to_be_fixed"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{id}", testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();
        System.out.println(task.getName());
        System.out.println(task.getIndex());
        System.out.println(task.getDescription());
        System.out.println(task.getTaskStatus().getSlug());
        System.out.println(task.getAssignee().getId());
        assertThat(task.getName()).isEqualTo("Task 1");
        assertThat(task.getIndex()).isEqualTo(9000);
        assertThat(task.getDescription()).isEqualTo("Description of task 1");
        assertThat(task.getTaskStatus().getSlug()).isEqualTo("to_be_fixed");
        assertThat(task.getAssignee().getId()).isEqualTo(1);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{id}", testTask.getId()).with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }
}
