package com.takehome.learningnavigator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.takehome.learningnavigator.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    // The JpaRepository already provides methods like findById, so no additional methods are needed unless custom queries are required.
}

