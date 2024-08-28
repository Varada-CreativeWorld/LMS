package com.takehome.learningnavigator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takehome.learningnavigator.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    Optional<Subject> findById(Long subjectId);
}

