package com.takehome.learningnavigator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takehome.learningnavigator.dto.SubjectResponse;
import com.takehome.learningnavigator.entity.Exam;
import com.takehome.learningnavigator.entity.Student;
import com.takehome.learningnavigator.entity.Subject;
import com.takehome.learningnavigator.exception.ResourceNotFoundException;
import com.takehome.learningnavigator.repository.ExamRepository;
import com.takehome.learningnavigator.repository.StudentRepository;
import com.takehome.learningnavigator.repository.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    public SubjectResponse addSubject(Subject subject) {
        // Validate the subject ID is unique
        if (subjectRepository.findById(subject.getId()).isPresent()) {
            throw new IllegalArgumentException("Subject with this ID already exists");
        }
        Subject result = subjectRepository.save(subject);
        return convertToSubjectResponse(result);
    }

    public List<SubjectResponse> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(this::convertToSubjectResponse)
                .collect(Collectors.toList());
    }

    public SubjectResponse getSubjectById(Long subjectId) {
        Subject subject =  subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        return convertToSubjectResponse(subject);
    }

    @Transactional
    public void deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
        for (Exam exam : subject.getExams()) {
            exam.setSubject(null);
        }
        examRepository.saveAll(subject.getExams());
        for (Student student : subject.getStudents()) {
            student.getEnrolledSubjects().remove(subject);
        }
        studentRepository.saveAll(subject.getStudents());
        subjectRepository.delete(subject);
    }


    private SubjectResponse convertToSubjectResponse(Subject subject) {
        SubjectResponse dto = new SubjectResponse();
        dto.setId(subject.getId());
        dto.setName(subject.getName());

        List<Long> studentIds = (subject.getStudents() != null) ?
            subject.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList()) :
            new ArrayList<>(); // Use empty list if null
        dto.setStudentIds(studentIds);

        List<Long> examIds = (subject.getExams() != null) ?
            subject.getExams().stream()
                    .map(Exam::getId)
                    .collect(Collectors.toList()) :
                new ArrayList<>(); // Use empty list if null
            dto.setExamIds(examIds);

        return dto;
    }
}

