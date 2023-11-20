package com.ages.informativoparaimigrantes.domain;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Tag {

    @Column()
    @Id
    private Long id;

    @Column()
    private String name;

    @Column()
    private String language;

    public Tag(Long id, String name, String language){
        this.id = id;
        this.name = name;
        this.language = language;
    }
}