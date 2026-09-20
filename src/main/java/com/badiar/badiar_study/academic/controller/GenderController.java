package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.response.GenderResponse;
import com.badiar.badiar_study.academic.service.GenderService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic/genders")
@RequiredArgsConstructor
public class GenderController {

    private final GenderService genderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GenderResponse>>> getAllGenders() {
        return ResponseEntity.ok(ApiResponse.success(genderService.getAllGenders()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GenderResponse>> getGenderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(genderService.getGenderById(id)));
    }
}