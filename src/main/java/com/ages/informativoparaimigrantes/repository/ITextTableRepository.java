package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.TextTableEntity;
import com.ages.informativoparaimigrantes.dto.TextTableResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ITextTableRepository extends JpaRepository<TextTableEntity, Long> {
    TextTableEntity findByLanguageAndTitleAndSequence(String language, String title, int sequence);
    List<TextTableEntity> findByLanguage(String language);
}
