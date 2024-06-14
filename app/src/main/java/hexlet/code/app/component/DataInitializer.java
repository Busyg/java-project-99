package hexlet.code.app.component;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.CustomUserDetailsService;
import hexlet.code.app.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private final TaskStatusService taskStatusService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        customUserDetailsService.createUser(userData);

        taskStatusService.create(new TaskStatusCreateDTO("Draft", "draft"));
        taskStatusService.create(new TaskStatusCreateDTO("To review", "to_review"));
        taskStatusService.create(new TaskStatusCreateDTO("To be fixed", "to_be_fixed"));
        taskStatusService.create(new TaskStatusCreateDTO("To publish", "to_publish"));
        taskStatusService.create(new TaskStatusCreateDTO("Published", "published"));
    }
}
