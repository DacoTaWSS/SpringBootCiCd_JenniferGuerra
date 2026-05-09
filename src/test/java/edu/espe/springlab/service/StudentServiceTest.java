package edu.espe.springlab.service;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentCreateRequest;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.impl.StudentServiceImpl;
import edu.espe.springlab.web.advice.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

@DataJpaTest
@Import(StudentServiceImpl.class)
public class StudentServiceTest {
    @Autowired
    private StudentServiceImpl service;

    @Autowired
    private StudentRepository repo;

    @Test
    void shouldNotAllowDuplicatedEmail(){
        Student existing = new Student();
        existing.setFullName("Existing");
        existing.setEmail("duplicate@example.com");
        existing.setBirthDate(LocalDate.of(2001,10,15));
        existing.setActive(true);
        repo.save(existing);

        StudentCreateRequest req = new StudentCreateRequest();
        req.setFullName("New User");
        req.setEmail("duplicate@example.com");
        req.setBirthDate(LocalDate.of(2001,10,15));

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(ConflictException.class);

    }
}
