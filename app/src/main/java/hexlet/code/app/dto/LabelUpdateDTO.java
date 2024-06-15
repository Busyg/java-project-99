package hexlet.code.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelUpdateDTO {
    @NotNull
    @Size(min = 3, max = 1000)
    @Column(unique = true)
    private String name;
}
