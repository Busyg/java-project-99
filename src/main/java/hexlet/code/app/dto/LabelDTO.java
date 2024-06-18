package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class LabelDTO {
    private Long id;
    private String name;
    private LocalDate createdAt;
}
