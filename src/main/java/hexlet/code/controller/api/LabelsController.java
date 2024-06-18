package hexlet.code.controller.api;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class LabelsController {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelMapper labelMapper;

    @GetMapping(path = "/labels")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelRepository.findAll();
        var result = labels.stream().map(labelMapper::map).toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(result);
    }

    @PostMapping(path = "/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO labelCreateDTO) {
        var label = labelMapper.map(labelCreateDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @PutMapping(path = "/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@RequestBody LabelUpdateDTO labelUpdateDTO, @PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        labelMapper.update(labelUpdateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @GetMapping(path = "/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return labelMapper.map(label);
    }

    @DeleteMapping(path = "/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelRepository.deleteById(id);
    }
}
