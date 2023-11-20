package com.ages.informativoparaimigrantes.domain.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class ArticleId implements Serializable {
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String language;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
