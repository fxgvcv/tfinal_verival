package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.dto.TextTableRequestDTO;
import com.ages.informativoparaimigrantes.dto.TextTableResponseDTO;

import java.util.List;
public interface ITextTableService {

    TextTableResponseDTO createTextTable(TextTableRequestDTO content);

    TextTableResponseDTO getTextTable(Long id);

    List<TextTableResponseDTO> getAllTexts();


    void deleteTextTable(Long id);

    TextTableResponseDTO findByLanguageAndTitleAndSequence(String language, String title, int sequence);
    List<TextTableResponseDTO> findByLanguage(String language);
}
