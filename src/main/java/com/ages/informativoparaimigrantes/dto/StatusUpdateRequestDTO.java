package com.ages.informativoparaimigrantes.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequestDTO {
    private String status;
    private String feedback;
}
