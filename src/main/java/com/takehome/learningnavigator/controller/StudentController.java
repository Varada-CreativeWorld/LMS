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

import com.takehome.learningnavigator.dto.StudentRequest;
import com.takehome.learningnavigator.dto.StudentResponse;
import com.takehome.learningnavigator.entity.Student;
import com.takehome.learningnavigator.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> registerStudent(@Valid @RequestBody StudentRequest studentRequest) {
        // Convert StudentRequest DTO to Student entity
        Student student = new Student();
        student.setId(studentRequest.getId());
        student.setName(studentRequest.getName());

        // Register the student using the service
        StudentResponse registeredStudent = studentService.registerStudent(student, studentRequest.getSubjectIds(), studentRequest.getExamIds());
        return ResponseEntity.ok(registeredStudent);
    }

    @PutMapping("/{studentId}/enroll-subject/{subjectId}")
    public ResponseEntity<StudentResponse> enrollInSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        StudentResponse updatedStudent = studentService.enrollInSubject(studentId, subjectId);
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{studentId}/register-exam/{examId}")
    public ResponseEntity<StudentResponse> registerForExam(@PathVariable Long studentId, @PathVariable Long examId) {
        StudentResponse updatedStudent = studentService.registerForExam(studentId, examId);
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{studentId}/unenroll-subject/{subjectId}")
    public ResponseEntity<StudentResponse> unenrollInSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        StudentResponse updatedStudent = studentService.unenrollInSubject(studentId, subjectId);
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{studentId}/deregister-exam/{examId}")
    public ResponseEntity<StudentResponse> deregisterForExam(@PathVariable Long studentId, @PathVariable Long examId) {
        StudentResponse updatedStudent = studentService.deregisterForExam(studentId, examId);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long studentId) {
        StudentResponse student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}

