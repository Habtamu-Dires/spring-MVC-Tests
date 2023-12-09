package com.example.student;

import com.example.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
* @Mock:
    Used to create a mock object for a class or interface.
    Commonly used in unit tests to mock dependencies of the class being tested.
    Does not have any direct association with the Spring context; it's used in plain unit testing.
* @InjectMocks: Used to inject mock objects (created with @Mock) into the fields of the class under test
* */

@ExtendWith(MockitoExtension.class)  // integrate mockito with Junit5 to use @Mock and @InjectMockes
class StudentServiceTest {

    @Mock  // used in unit tests to mock instance of a class or interface which are dependencies.
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll(); // the mock was called with findAll() method.
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(
                "Hawa",
                "hawa@gmail.com"
        );
        // when
        underTest.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student  capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "Hawa",
                "hawa@gmail.com"
        );
        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " token");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void canUpdateNameAndEmailOfStudent(){
        //given
        Long id = 1L;
        Student student = new Student("Abebe", "abe@gamil.com");
        Student newStudent = new Student("Kebede", "kbe@gamil.com");
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        given(studentRepository.selectExistsEmail(any()))
                .willReturn(false);
        //when
        Student stu = underTest.updateStudent(id, newStudent);
        //then
        assertThat(stu.getName()).isEqualTo(newStudent.getName());
        assertThat(stu.getEmail()).isEqualTo(newStudent.getEmail());
    }

    @Test
    void willThrowErrorWhenEmailIsTakenOnUpdate(){
        //given
        Long id = 1L;
        Student student = new Student("Abebe", "abe@gamil.com");
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        given(studentRepository.selectExistsEmail(any()))
                .willReturn(true);
        //when
        //then
       assertThatThrownBy(() -> underTest.updateStudent(id, student))
               .isInstanceOf(RuntimeException.class)
               .hasMessageContaining("Email " + student.getEmail() + " token");

       verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        //given
        Long id = 1L;
        //when
        underTest.deleteStudent(id);
        //then
        ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);

        verify(studentRepository).deleteById(idCapture.capture());

        Long capturedId = idCapture.getValue();

        assertThat(capturedId).isEqualTo(id);
    }

}