package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.dto.TagResponseDTO;
import com.ages.informativoparaimigrantes.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService{
    private final ITagRepository repository;

    @Autowired
    public TagServiceImpl(ITagRepository repository) {
        this.repository = repository;
    }

    public List<TagResponseDTO> getAll(String language) {
        return repository.findByLanguage(language).stream().map(t -> TagResponseDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .language(t.getLanguage())
                .build()).collect(Collectors.toList());
    }
}
