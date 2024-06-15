package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapAssignee")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "mapTaskStatus")
    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "mapLabels")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "mapLabelIds")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "mapAssignee")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "mapTaskStatus")
    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "mapLabels")
    public abstract void update(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);

    @Named("mapTaskStatus")
    public TaskStatus mapTaskStatus(String status) {
        return taskStatusRepository.findBySlug(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
    }

    @Named("mapAssignee")
    public User mapAssignee(Long id) {
        return id == null ? null : userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
    }

    @Named("mapLabels")
    public Set<Label> mapLabels(Set<Long> labelIds) {
        System.out.println("labelIds: " + labelIds);
        return labelIds == null ? new HashSet<>() : labelRepository.findAllByIdIn(labelIds);
    }

    @Named("mapLabelIds")
    public Set<Long> mapLabelIds(Set<Label> labels) {
        System.out.println("labels: " + labels);
        return labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}
