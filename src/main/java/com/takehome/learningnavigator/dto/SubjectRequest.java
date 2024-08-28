package com.takehome.learningnavigator.dto;

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
public class SubjectRequest {

    @NotNull(message = "Subject ID is required")
    private Long id;

    @NotBlank(message = "Subject name is required")
    private String name;
}
