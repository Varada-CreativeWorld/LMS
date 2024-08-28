package com.takehome.learningnavigator.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long examId;
    private Long subjectId;
    private String subjectName;
    private List<Long> registeredStudentIds;
}
