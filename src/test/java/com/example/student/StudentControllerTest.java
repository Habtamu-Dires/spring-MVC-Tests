package com.example.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class) // bridge b/n junit test and spring boot. older
//@EXtendWith(SpringExtension.class) -- for junit 5 enables integration of the spring
// context with JUnit5 test. it replaces the older @RunWith used in JUnit4
/* @WebMvcTest -- provided by spring boot to test web layers of the application.
* simulate the behaviour of real HTTP requests.
*StudentController.class tells spring boot to setup limited resources..i.e only necessary
* components for testing the specified controller.
*
* @MockBean    Used in Spring tests to replace a bean in the Spring context with a mock or spy.
    Specifically used for integration tests to mock or spy on beans within the Spring application context.
    Works with the Spring context and is used in combination with @SpringBootTest or @WebMvcTest.
* */


@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void canGetAllStudents() throws Exception{
        //given
        List<Student> students = new ArrayList<>();
        students.add(new Student("Abebe", "abe@gamil.com"));
        students.add(new Student("Alex", "alex@gmail.com"));
        //then
        when(studentService.getAllStudents())
                .thenReturn(students);
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Abebe"))
                .andExpect(jsonPath("$[0].email").value("abe@gamil.com"))
                .andExpect(jsonPath("$[1].name").value("Alex"))
                .andExpect(jsonPath("$[1].email").value("alex@gmail.com"));
    }

    @Test
    void addStudent() throws Exception{
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Student studentToAdd = new Student("Abebe", "abe@gamil.com");
        //convert into a json string
        String jsonString = objectMapper.writeValueAsString(studentToAdd);
        //when
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
        //then
        verify(studentService, times(1)).addStudent(studentToAdd);

    }

    @Test
    void canUpdateStudent() throws Exception{
        //given
        Long id = 1L;
        Student student = new Student("Abebe", "abe@gamil.com");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(student);
        System.out.println(jsonString);
        System.out.println(student);
        //when
        when(studentService.updateStudent(id, student))
                .thenReturn(student);
        //then
        mockMvc.perform(put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(student.getName()))
                .andExpect(jsonPath("email").value(student.getEmail()));
    }

    @Test
    void deleteStudent() throws Exception{
        //given
        Long id = 1L;
        //when
        mockMvc.perform(delete("/students/"+id))
                .andExpect(status().isOk());
        //then
        verify(studentService).deleteStudent(id);
    }
}