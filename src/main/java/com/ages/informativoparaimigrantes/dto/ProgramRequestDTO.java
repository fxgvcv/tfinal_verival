package com.ages.informativoparaimigrantes.dto;
import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.enums.*;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProgramRequestDTO {

    private String title;
    private String institutionEmail;
    private String description;
    private String link;
    private String programInitialDate;
    private String programEndDate;
    private String language;
    private String enrollmentInitialDate;
    private String enrollmentEndDate;
    private Status status;
    private String location;
    private List<TagRequestDTO> tags;
    private String file;
    private ProgramType programType;
}












