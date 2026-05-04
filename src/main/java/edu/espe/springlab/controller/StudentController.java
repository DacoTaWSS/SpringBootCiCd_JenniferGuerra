package edu.espe.springlab.controller;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentCreateRequest;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.dto.StudentUpdateRequest;
import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.web.advice.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    //Inyeccion de dependencia
    private final StudentService service;

    public StudentController (StudentService service){
        this.service = service;
    }

    //Crear un ES
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest request) throws NotFoundException{
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    //Obtener un ES por ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentbyId(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    //Obtener TODOS los ES
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents(){
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponse>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.search(name, page, size));
    }

    @PatchMapping("/{id}/deactive")//Actualiza algo especifico ...
    public ResponseEntity<StudentResponse> deactivateStudent(@PathVariable Long id){
        return ResponseEntity.ok(service.deactivate(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
