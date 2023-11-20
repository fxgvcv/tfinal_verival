package com.ages.informativoparaimigrantes.dto;

import com.ages.informativoparaimigrantes.enums.InstitutionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionRegistrationDTO {
    private String institutionName;

    private String email;

    private String cnpj;

    private InstitutionType type;

    private String registrantName;

    private String registrantCpf;

    private String registrantRole;

    private String phone;

    private String attachment;

    private String password;

    public InstitutionRegistrationDTO(String institutionName, String email, String cnpj, String type, String registrantName, String registrantCpf, String registrantRole, String phone, String attachment) {
        this.institutionName = institutionName;
        this.email = email;
        this.cnpj = cnpj;
        this.type = InstitutionType.valueOf(type);
        this.registrantName = registrantName;
        this.registrantCpf = registrantCpf;
        this.attachment = attachment;
        this.registrantRole = registrantRole;
        this.phone = phone;
    }
}
