package com.example.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public List<Student> getAllStudents(){
       return service.getAllStudents();
    }

    @PostMapping
    public void addStudent(@RequestBody Student student) {
        service.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable("id") Long id,  @RequestBody Student student){
        return service.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") Long id){
        service.deleteStudent(id);
    }

}
