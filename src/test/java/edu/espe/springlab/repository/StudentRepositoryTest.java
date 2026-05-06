package edu.espe.springlab.repository;

import edu.espe.springlab.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository repo;

    @Test
    void shoulSaveandFindStudentByEmail(){
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setFullName("Test User");
        student.setBirthDate(LocalDate.of(2001,10,15));
        student.setActive(true);
        repo.save(student);

        var result = repo.findByEmail("test@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Test User");
    }
}
