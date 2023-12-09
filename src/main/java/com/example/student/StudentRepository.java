package com.example.student;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 " +
            "THEN TRUE ELSE FALSE END FROM Student s WHERE s.email = ?1")
    Boolean selectExistsEmail(String email);

//    @Query(value = "SELECT id FROM Student WHERE email=?", nativeQuery = true)
//    Long getIdByEmail(String email);

}
