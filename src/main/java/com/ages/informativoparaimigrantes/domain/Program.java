package com.ages.informativoparaimigrantes.domain;

import com.ages.informativoparaimigrantes.enums.ProgramType;
import com.ages.informativoparaimigrantes.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter // Lombok irá gerar os getters para todos os campos
@Entity
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String institutionEmail;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String link;

    private String language;

    private Date programInitialDate;

    private Date programEndDate;

    private Date enrollmentInitialDate;

    private Date enrollmentEndDate;

    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

    private String location;

    @ManyToMany
    private List<Tag> tags;

    private String dataFile;

    @Enumerated(EnumType.STRING)
    private ProgramType programType;

    @Setter
    private String feedback;
}
