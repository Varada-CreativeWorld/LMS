package com.takehome.learningnavigator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takehome.learningnavigator.dto.StudentResponse;
import com.takehome.learningnavigator.entity.Exam;
import com.takehome.learningnavigator.entity.Student;
import com.takehome.learningnavigator.entity.Subject;
import com.takehome.learningnavigator.exception.ResourceNotFoundException;
import com.takehome.learningnavigator.repository.ExamRepository;
import com.takehome.learningnavigator.repository.StudentRepository;
import com.takehome.learningnavigator.repository.SubjectRepository;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    public StudentResponse registerStudent(Student student, List<Long> subjectIds, List<Long> examIds) {
        // Validate the registration ID is unique
        if (studentRepository.findById(student.getId()).isPresent()) {
            throw new IllegalArgumentException("Student with registration ID already exists");
        }
        List<Subject> subjects = new ArrayList<>();
        List<Exam> exams = new ArrayList<>();
        if(!subjectIds.isEmpty()){
            for(Long subjectId: subjectIds){
                Subject temp = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));
                subjects.add(temp);
            }
        }
        if(!examIds.isEmpty()){
            for(Long examId: examIds){
                Exam temp = examRepository.findById(examId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));
                exams.add(temp);            
            }
        }
        student.setEnrolledSubjects(subjects);
        student.setRegisteredExams(exams);
        Student result = studentRepository.save(student);
        return convertToStudentResponse(result);
    }

    public StudentResponse enrollInSubject(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));

        // Check if student is already enrolled in the subject
        if (student.getEnrolledSubjects().contains(subject)) {
            throw new IllegalArgumentException("Student already enrolled in the subject");
        }

        student.getEnrolledSubjects().add(subject);
        subject.getStudents().add(student);
        subjectRepository.save(subject);
        Student result = studentRepository.save(student);
        return convertToStudentResponse(result);
    }

    public StudentResponse unenrollInSubject(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + subjectId));

        // Check if student is enrolled in the subject
        if (!student.getEnrolledSubjects().contains(subject)) {
            throw new IllegalArgumentException("Student is not enrolled in the subject");
        }

        ////////////////////////////////////////////////

        student.getEnrolledSubjects().remove(subject);

        List<Exam> filteredExams = student.getRegisteredExams().stream()
            .filter(exam -> exam.getSubject().getId() == subjectId)
            .collect(Collectors.toList());

        student.getRegisteredExams().removeAll(filteredExams);

        ////////////////////////////////////////////////

        subject.getStudents().remove(student);
        subjectRepository.save(subject);

        ////////////////////////////////////////////////

        filteredExams.stream()
            .peek(exam -> exam.getEnrolledStudents().remove(student)) // Modify each exam
            .collect(Collectors.toList()); // Collect the modified exams (optional, as we directly modify them)

        examRepository.saveAll(filteredExams);

        ////////////////////////////////////////////////
        Student result = studentRepository.save(student);
        return convertToStudentResponse(result);
    }

    public StudentResponse registerForExam(Long studentId, Long examId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        // Check if student is enrolled in the subject of the exam
        if (!student.getEnrolledSubjects().contains(exam.getSubject())) {
            throw new IllegalArgumentException("Student must be enrolled in the subject before registering for the exam");
        }

        // Check if student is already registered for the exam
        if (student.getRegisteredExams().contains(exam)) {
            throw new IllegalArgumentException("Student already registered for the exam");
        }

        student.getRegisteredExams().add(exam);
        exam.getEnrolledStudents().add(student);

        // Save changes to both student and exam entities
        examRepository.save(exam);
        Student result = studentRepository.save(student);
        return convertToStudentResponse(result);
    }

    public StudentResponse deregisterForExam(Long studentId, Long examId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));


        // Check if student is registered for the exam
        if (!student.getRegisteredExams().contains(exam)) {
            throw new IllegalArgumentException("Student has not registered for the exam");
        }

        student.getRegisteredExams().remove(exam);
        exam.getEnrolledStudents().remove(student);

        // Save changes to both student and exam entities
        examRepository.save(exam);
        Student result = studentRepository.save(student);
        return convertToStudentResponse(result);
    }

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToStudentResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        return convertToStudentResponse(student);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        for (Subject subject : student.getEnrolledSubjects()) {
            subject.getStudents().remove(student);
        }
        subjectRepository.saveAll(student.getEnrolledSubjects());

        for (Exam exam : student.getRegisteredExams()) {
            exam.getEnrolledStudents().remove(student);
        }
        examRepository.saveAll(student.getRegisteredExams());
        studentRepository.delete(student);
    }

    private StudentResponse convertToStudentResponse(Student student) {
        StudentResponse dto = new StudentResponse();
        dto.setId(student.getId());
        dto.setName(student.getName());

        // Use IDs to avoid circular reference
        List<Long> subjectIds = (student.getEnrolledSubjects() != null) ? 
        student.getEnrolledSubjects().stream()
            .map(Subject::getId)
            .collect(Collectors.toList()) :
        new ArrayList<>();
        dto.setEnrolledSubjectIds(subjectIds);

        List<Long> examIds = (student.getRegisteredExams() != null) ?
        student.getRegisteredExams().stream()
                .map(Exam::getId)
                .collect(Collectors.toList()):
        new ArrayList<>();
        dto.setRegisteredExamIds(examIds);

        return dto;
    }
}

