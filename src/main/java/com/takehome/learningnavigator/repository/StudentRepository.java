package com.takehome.learningnavigator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takehome.learningnavigator.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findById(Long registrationId);
}

