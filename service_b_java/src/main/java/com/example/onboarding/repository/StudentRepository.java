package com.example.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.onboarding.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}