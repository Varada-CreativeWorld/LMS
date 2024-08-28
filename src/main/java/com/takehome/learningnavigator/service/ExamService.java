package com.takehome.learningnavigator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takehome.learningnavigator.dto.ExamResponse;
import com.takehome.learningnavigator.entity.Exam;
import com.takehome.learningnavigator.entity.Student;
import com.takehome.learningnavigator.entity.Subject;
import com.takehome.learningnavigator.exception.ResourceNotFoundException;
import com.takehome.learningnavigator.repository.ExamRepository;
import com.takehome.learningnavigator.repository.StudentRepository;
import com.takehome.learningnavigator.repository.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    public ExamResponse createExam(Exam exam) {
        // Validate the subject exists before creating the exam
        if (examRepository.findById(exam.getId()).isPresent()) {
            throw new IllegalArgumentException("Exam with given ID already exists");
        }
        Subject subject = subjectRepository.findById(exam.getSubject().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + exam.getSubject().getId()));

        exam.setSubject(subject);
        subject.getExams().add(exam);
        subjectRepository.save(subject);
        Exam result = examRepository.save(exam);
        return convertToExamResponse(result);
    }

    public List<ExamResponse> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream()
            .map(this::convertToExamResponse)
            .collect(Collectors.toList());
    }

    public ExamResponse getExamById(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));
        return convertToExamResponse(exam);
    }

    public ExamResponse updateExamSubject(Long examId, Long subjectId){
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));        
        Subject subject = subjectRepository.findById(exam.getSubject().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + exam.getSubject().getId()));

        subject.getExams().remove(exam);
        exam.setSubject(subject);
        subject.getExams().add(exam);
        subjectRepository.save(subject);
        Exam result = examRepository.save(exam);
        return convertToExamResponse(result);
    }

    @Transactional
    public void deleteExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));
        for (Student student : exam.getEnrolledStudents()) {
            student.getRegisteredExams().remove(exam);
        }
        studentRepository.saveAll(exam.getEnrolledStudents());
        examRepository.delete(exam);
    }

    private ExamResponse convertToExamResponse(Exam exam) {
        ExamResponse dto = new ExamResponse();
        dto.setExamId(exam.getId());
        dto.setSubjectId(exam.getSubject().getId());
        dto.setSubjectName(exam.getSubject().getName());

        // Check if students list is null and handle accordingly
        List<Long> studentIds = (exam.getEnrolledStudents() != null) ?
            exam.getEnrolledStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList()) :
            new ArrayList<>(); // Use empty list if null
        dto.setRegisteredStudentIds(studentIds);

        return dto;
    }
}

