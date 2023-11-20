package com.ages.informativoparaimigrantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TextTableResponseDTO {

    private Long id;
    private String content;
    private String language;
    private String title;
    private int sequence;

}
