package com.ages.informativoparaimigrantes.dto;

import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProgramResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String link;
    private String language;
    private Date programInitialDate;
    private Date programEndDate;
    private Date enrollmentInitialDate;
    private Date enrollmentEndDate;
    private Status status;
    private String institutionEmail;
    private String location;
    private List<Tag> tags;
    private String file;
    private ProgramType programType;
    private String feedback;
}
