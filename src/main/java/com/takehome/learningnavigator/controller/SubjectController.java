package com.takehome.learningnavigator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.takehome.learningnavigator.dto.SubjectRequest;
import com.takehome.learningnavigator.dto.SubjectResponse;
import com.takehome.learningnavigator.entity.Subject;
import com.takehome.learningnavigator.service.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponse> addSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        // Convert SubjectRequest DTO to Subject entity
        Subject subject = new Subject();
        subject.setId(subjectRequest.getId());
        subject.setName(subjectRequest.getName());
        
        // Register the subject using the service
        SubjectResponse addedSubject = subjectService.addSubject(subject);
        return ResponseEntity.ok(addedSubject);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        List<SubjectResponse> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long subjectId) {
        SubjectResponse subject = subjectService.getSubjectById(subjectId);
        return ResponseEntity.ok(subject);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.noContent().build();
    }
}

