package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.dto.TagResponseDTO;

import java.util.List;
public interface ITagService {
    List<TagResponseDTO> getAll(String language);
}
