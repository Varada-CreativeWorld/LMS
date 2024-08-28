package com.takehome.learningnavigator.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotNull(message = "Student ID is required")
    private Long id;

    @NotBlank(message = "Student name is required")
    private String name;

    private List<Long> subjectIds = new ArrayList<>();
    private List<Long> examIds = new ArrayList<>();
}

