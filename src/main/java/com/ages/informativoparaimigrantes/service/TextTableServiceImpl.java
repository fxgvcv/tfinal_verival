package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.domain.TextTableEntity;
import com.ages.informativoparaimigrantes.dto.TextTableRequestDTO;
import com.ages.informativoparaimigrantes.dto.TextTableResponseDTO;
import com.ages.informativoparaimigrantes.repository.ITextTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextTableServiceImpl implements ITextTableService{
    private final ITextTableRepository repository;

    @Autowired
    public TextTableServiceImpl(ITextTableRepository repository) {
        this.repository = repository;
    }

    public List<TextTableResponseDTO> getAllTexts() {
        return repository.findAll().stream().map(t -> TextTableResponseDTO.builder()
                .id(t.getId())
                .language(t.getLanguage())
                .title(t.getTitle())
                .sequence(t.getSequence())
                .content(t.getContent())
                .build()).collect(Collectors.toList());
    }


    public TextTableResponseDTO createTextTable(@NotNull TextTableRequestDTO text) {
        TextTableEntity newText = TextTableEntity.builder()
                .content(text.getContent())
                .title(text.getTitle())
                .language(text.getLanguage())
                .sequence(text.getSequence())
                .build();
        //Checks if text already exists
        TextTableEntity textExists = repository.findByLanguageAndTitleAndSequence(text.getLanguage(), text.getTitle(), text.getSequence());
        if (textExists != null) {
            return TextTableResponseDTO.builder()
                    .id(textExists.getId())
                    .title(textExists.getTitle())
                    .sequence(textExists.getSequence())
                    .language(textExists.getLanguage())
                    .content(textExists.getContent())
                    .build();
        }
        TextTableEntity textSaved = repository.save(newText);
        return TextTableResponseDTO.builder()
                .id(textSaved.getId())
                .title(textSaved.getTitle())
                .sequence(textSaved.getSequence())
                .language(textSaved.getLanguage())
                .content(textSaved.getContent())
                .build();
    }

    public TextTableResponseDTO getTextTable(Long id) {
        TextTableEntity text = repository.findById(id).orElseThrow(() -> new RuntimeException("Text not found"));
        return TextTableResponseDTO.builder()
                .id(text.getId())
                .title(text.getTitle())
                .language(text.getLanguage())
                .content(text.getContent())
                .sequence(text.getSequence())
                .build();
    }

    public TextTableResponseDTO findByLanguageAndTitleAndSequence(String language, String title, int sequence) {
        TextTableEntity text = repository.findByLanguageAndTitleAndSequence(language, title, sequence);
        return TextTableResponseDTO.builder()
                .id(text.getId())
                .title(text.getTitle())
                .language(text.getLanguage())
                .content(text.getContent())
                .sequence(text.getSequence())
                .build();
    }
    public List<TextTableResponseDTO> findByLanguage(String language) {
        List<TextTableEntity> texts = repository.findByLanguage(language);

        return texts.stream()
                .map(text -> TextTableResponseDTO.builder()
                        .id(text.getId())
                        .title(text.getTitle())
                        .language(text.getLanguage())
                        .content(text.getContent())
                        .sequence(text.getSequence())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteTextTable(Long id) {
        repository.deleteById(id);
    }
}
