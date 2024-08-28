package com.takehome.learningnavigator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequest {

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

}
