package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private Timestamp createdAt;
}
