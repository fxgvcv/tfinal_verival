package com.ages.informativoparaimigrantes.domain;

import lombok.*;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class TextTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String content;
    @NonNull
    private String language;
    @NonNull
    private String title;
    private int sequence;
}
