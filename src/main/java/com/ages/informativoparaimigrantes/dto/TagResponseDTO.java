package com.ages.informativoparaimigrantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TagResponseDTO {

    private Long id;
    private String name;
    private String language;
}
