package com.example.onboarding.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.onboarding.entity.Student;
import com.example.onboarding.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/batch")
    public String saveBatch(@RequestBody List<Student> students) {
        service.saveAll(students);
        return "Saved " + students.size() + " students";
    }
}