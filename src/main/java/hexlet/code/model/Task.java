package hexlet.code.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private Integer index;

    private String description;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    /*
        Если убрать cascade, получаю ошибку org.hibernate.TransientPropertyValueException:
        object references an unsaved transient instance - save the transient instance before flushing :
        hexlet.code.model.Task.taskStatus -> hexlet.code.model.TaskStatus
    */
    private TaskStatus taskStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    /*
        Если убрать cascade, получаю ошибку org.hibernate.TransientPropertyValueException:
        object references an unsaved transient instance - save the transient instance before flushing :
        hexlet.code.model.Task.assignee -> hexlet.code.model.User
    */
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    /*
        Если убрать cascade, получаю ошибку org.hibernate.TransientPropertyValueException:
        object references an unsaved transient instance - save the transient instance before flushing :
        hexlet.code.model.Label
    */
    private Set<Label> labels = new HashSet<>();
}
