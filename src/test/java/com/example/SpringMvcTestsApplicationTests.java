package com.example;

import com.example.student.Student;
import com.example.student.StudentController;
import com.example.student.StudentRepository;
import com.example.student.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
 * @SpringBootTest - show it is integration test, it loads the entire application context
 * including all beans, configurations & components
 * */
/* @AutoConfigureMockMvc -> ensures that a MockMvc instance is created and available for
 *  integration testing.
 * @DataJpaTest - can't be used along with @SpringBootTest - used for specific
 * @WebMvcTest - can't be used along with @SpringBootTest - used for specific
 * */

@SpringBootTest
@AutoConfigureMockMvc
class SpringMvcTestsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentRepository studentRepository;

	@BeforeEach
	void setup(){
		studentRepository.deleteAll();
	}

	@Test
	public void canGetAllStudent() throws Exception{
		//given
		Student student = new Student("Abebe", "abe@gamil.com");
		//when
		studentService.addStudent(student);
		//then
		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Abebe"))
				.andExpect(jsonPath("$[0].email").value("abe@gamil.com"));
	}

	@Test
	public void canUpdateStudent() throws Exception{
		//given
		Student oldStudent = new Student("Abebe", "abe@gamil.com");
		studentService.addStudent(oldStudent);

		Student newStudent = new Student("Kebede", "kebe@gamil.com");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(newStudent);
		//when
		mockMvc.perform(put("/students/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(status().isOk());
		//then
		List<Student> students = studentService.getAllStudents();
		assertThat(students.size()).isEqualTo(1);
		assertThat(students.getFirst().getName()).isEqualTo(newStudent.getName());
		assertThat(students.getFirst().getEmail()).isEqualTo(newStudent.getEmail());
	}

	@Test
	public void canDeleteStudent() throws Exception{
		//given
		Student student = new Student("Abebe", "abe@gamil.com");
		studentService.addStudent(student);
		//when
		List<Student> allStudents = studentService.getAllStudents();
		Long id = allStudents.getFirst().getId();
		mockMvc.perform(delete("/students/{id}", id))
				.andExpect(status().isOk());

		//then
		allStudents = studentService.getAllStudents();
		assertThat(allStudents).isEmpty();

	}


}
