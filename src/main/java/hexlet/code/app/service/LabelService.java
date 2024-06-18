package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        List<Label> labels = labelRepository.findAll();
        return labels.stream().map(labelMapper::map).toList();
    }

    public LabelDTO create(LabelCreateDTO labelCreateDTO) {
        var label = labelMapper.map(labelCreateDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO findById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return labelMapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO labelUpdateDTO, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        labelMapper.update(labelUpdateDTO, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
