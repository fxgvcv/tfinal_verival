package com.ages.informativoparaimigrantes.domain;

import com.ages.informativoparaimigrantes.enums.InstitutionType;
import com.ages.informativoparaimigrantes.enums.Status;
import com.ages.informativoparaimigrantes.dto.InstitutionRegistrationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Institution {

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "institution_email")
    @Id
    private String institutionEmail;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InstitutionType type;

    @Column(name = "registrant_name")
    private String registrantName;

    @Column(name = "registrant_cpf")
    private String registrantCpf;

    @Column(name = "registrant_role")
    private String registrantRole;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "feedback")
    private String feedback;

    public Institution(InstitutionRegistrationDTO dto){
        this.institutionName = dto.getInstitutionName();
        this.institutionEmail = dto.getEmail();
        this.cnpj = dto.getCnpj();
        this.type = dto.getType();
        this.registrantName = dto.getRegistrantName();
        this.registrantCpf = dto.getRegistrantCpf();
        this.registrantRole = dto.getRegistrantRole();
        this.phone = dto.getPhone();
        this.status = Status.PENDING;
        this.attachment = dto.getAttachment();
        this.feedback = "";
    }

    public Institution(String institutionName, String email, String cnpj, String type, String registrantName, String registrantCpf, String registrantRole, String phone, String status, String attachment) {
        this.institutionName = institutionName;
        this.institutionEmail = email;
        this.cnpj = cnpj;
        this.type = InstitutionType.valueOf(type);
        this.registrantName = registrantName;
        this.registrantCpf = registrantCpf;
        this.registrantRole = registrantRole;
        this.phone = phone;
        this.status = Status.valueOf(status);
        this.attachment = attachment;
    }
}
