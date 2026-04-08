package com.example.onboarding.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.onboarding.entity.Student;
import com.example.onboarding.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public void saveAll(List<Student> students) {
        repo.saveAll(students);
    }
}