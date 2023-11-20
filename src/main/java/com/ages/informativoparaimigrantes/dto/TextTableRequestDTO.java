package com.ages.informativoparaimigrantes.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TextTableRequestDTO {

    private String language;
    private String title;
    private String content;
    private int sequence;
    }

