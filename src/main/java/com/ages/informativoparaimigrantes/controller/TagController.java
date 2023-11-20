package com.ages.informativoparaimigrantes.controller;

import com.ages.informativoparaimigrantes.domain.Tag;
import com.ages.informativoparaimigrantes.dto.ProgramRequestDTO;
import com.ages.informativoparaimigrantes.dto.ProgramResponseDTO;
import com.ages.informativoparaimigrantes.dto.TagResponseDTO;
import com.ages.informativoparaimigrantes.service.ITagService;
import com.ages.informativoparaimigrantes.service.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagServiceImpl service;
    @Autowired
    public TagController(TagServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAll(@RequestParam() String language)
    {
        List<TagResponseDTO> tags = service.getAll(language);

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
