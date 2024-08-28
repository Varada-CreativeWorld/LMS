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
public class StudentResponse {
    private Long id;
    private String name;
    private List<Long> enrolledSubjectIds;
    private List<Long> registeredExamIds;
}
