package com.ages.informativoparaimigrantes.service;

import com.ages.informativoparaimigrantes.dto.ProgramRequestDTO;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.enums.Status;

public interface IProgramService {

   ProgramResponseDTO save (ProgramRequestDTO program);

   ProgramResponseDTO save(ProgramResponseDTO program);

   ProgramResponseDTO updateStatus(Long id, Status status, String feedback);
   long getCountByStatus(Status status);
}
