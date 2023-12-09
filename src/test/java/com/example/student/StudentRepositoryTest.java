package com.example.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
/* @DataJpaTest - designed for testing JPA layers of an application- repositories
 it provides in memory database or use application.yml in the test/resource folder if defined
 it load the spring application context in limited way - only components that are
 relevant to JPA - like repositories and entity managers. Which makes the test focused and faster

*
* */

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @BeforeEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentEmailExists() {
        //given
        String email = "Hawa@gmail.com";
        Student student = new Student(
                "Hawa",
                email
        );
        underTest.save(student);

        //when
        Boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentsEmailDoesNotExists() {
        //given
        String email = "Hawa@gmail.com";

        //when
        Boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isFalse();
    }

}