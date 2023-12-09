package com.example.student;

import com.example.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    public List<Student> getAllStudents(){
        System.out.println("getAllStudents inside a StudentService called.");
        return repository.findAll();
    }

    public void addStudent(Student student){
        Boolean existsEmail = repository
                .selectExistsEmail(student.getEmail());
        if(existsEmail){
            throw new BadRequestException("Email " + student.getEmail() + " token");
        }
        repository.save(student);
    }

    public void deleteStudent(Long id){
        repository.deleteById(id);
    }

    public Student updateStudent(Long id, Student student) {
       Student old = repository.findById(id)
                .orElse(null);

       if(old != null){
           Student newStudent = new Student();
           newStudent.setId(old.getId());
           newStudent.setName(student.getName());
           //check if the client want to change the email by providing new email
           if(!old.getName().equalsIgnoreCase(student.getEmail())){
               //check if email is not taken and update
               newStudent.setEmail(setEmailAddress(student));
           } else{
               newStudent.setEmail(old.getEmail());
           }
           repository.save(newStudent);
           return newStudent;
       } else{
           return null;
       }

    }

    public String setEmailAddress(Student student){
            Boolean emailExists = repository.selectExistsEmail(student.getEmail());
            if(emailExists){
                throw new BadRequestException("Email " + student.getEmail() + " token");
            }
            return student.getEmail();
    }
}

