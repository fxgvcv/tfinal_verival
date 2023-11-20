package com.ages.informativoparaimigrantes.dto;

import com.ages.informativoparaimigrantes.enums.InstitutionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InstitutionUserDTO {
    private String institutionName;
    private String email;
    private String cnpj;
    private InstitutionType type;
    private String registrantName;
    private String registrantCpf;
    private String registrantRole;
    private String phone;
    private String attachment;
    private String passwordOld;
    private String passwordNew;

}
