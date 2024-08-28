package com.takehome.learningnavigator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.learningnavigator.dto.ExamRequest;
import com.takehome.learningnavigator.dto.ExamResponse;
import com.takehome.learningnavigator.entity.Exam;
import com.takehome.learningnavigator.entity.Subject;
import com.takehome.learningnavigator.service.ExamService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody ExamRequest examRequest) {
        // Convert ExamRequest DTO to Exam entity

        Exam exam = new Exam();
        exam.setId(examRequest.getExamId());
        Subject subject=new Subject();
        subject.setId(examRequest.getSubjectId());
        exam.setSubject(subject);
        
        // Register the student using the service
        ExamResponse createdExam = examService.createExam(exam);
        return ResponseEntity.ok(createdExam);
    }


    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        List<ExamResponse> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable Long examId) {
        ExamResponse exam = examService.getExamById(examId);
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/{examId}/update-subject/{subjectId}")
    public ResponseEntity<ExamResponse> updateExamSubject(@PathVariable Long examId, @PathVariable Long subjectId) {
        ExamResponse examResponse = examService.updateExamSubject(examId, subjectId);
        return ResponseEntity.ok(examResponse);
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}

