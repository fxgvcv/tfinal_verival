package com.ages.informativoparaimigrantes.repository;

import com.ages.informativoparaimigrantes.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {
    @Query(value = "SELECT tag FROM Tag tag" +
            " WHERE (tag.language = :language)")
    List<Tag> findByLanguage(String language);

    Tag findByLanguageAndName(String language, String name);

}
