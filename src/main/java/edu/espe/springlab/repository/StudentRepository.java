package edu.espe.springlab.repository;

import edu.espe.springlab.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    //Buscar ES por email
    Optional<Student> findByEmail(String email);

    //Respuesta si existe al menos un registro
    boolean existsByEmail(String email);

    Page<Student> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
}
