package hexlet.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "labels")
@EntityListeners(AuditingEntityListener.class)
public class Label implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 1000)
    @Column(unique = true, columnDefinition = "TEXT")
    private String name;

    @CreatedDate
    private LocalDate createdAt;
}
