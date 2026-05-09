package edu.espe.springlab.service.impl;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentCreateRequest;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.dto.StudentUpdateRequest;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.web.advice.ConflictException;
import edu.espe.springlab.web.advice.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    //inyeccion de dependencia
    private final StudentRepository repo;
    public StudentServiceImpl(StudentRepository repo){
        this.repo = repo;
    }
    @Override
    public StudentResponse create(StudentCreateRequest request) {
        //aqui sabemos si dentro de base de datos tenemos el correo
        /*
            if(repo.existsByEmail(request.getEmail())){
                throw new ConflictException("El email ya esta registrado");
            }*/
        Student s = new Student();
        s.setFullName(request.getFullName());
        s.setEmail(request.getEmail());
        s.setBirthDate(request.getBirthDate());
        s.setActive(true);

        Student saved = repo.save(s);
        return toResponse(saved);
    }
    @Override
    public StudentResponse getById(Long id) {
        Student s = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro el registro"));
        return toResponse(s);
    }
    @Override
    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }
    @Override
    public StudentResponse deactivate(Long id) {
        Student s = repo.findById(id).orElseThrow(()-> new
                NotFoundException("ES no encontrado"));
        s.setActive(false);
        return toResponse(repo.save(s));
    }
    @Override
    public Page<StudentResponse> search(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Student> result;
        if (name != null && !name.isBlank()) {
            result = repo.findByFullNameContainingIgnoreCase(name, pageable);
        } else {
            result = repo.findAll(pageable);
        }

        return result.map(this::toResponse);
    }
    private StudentResponse toResponse(Student s){
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setFullName(s.getFullName());
        r.setEmail(s.getEmail());
        r.setBirthDate(s.getBirthDate());
        r.setActive(s.getActive());
        return r;
    }
    @Override
    public StudentResponse update(Long id, StudentUpdateRequest request) {
        // 1. Buscamos si el estudiante existe
        Student s = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el estudiante con ID: " + id));

        // 2. Opcional: Validar si el nuevo email ya lo tiene OTRO estudiante
        if (!s.getEmail().equals(request.getEmail()) && repo.existsByEmail(request.getEmail())) {
            throw new ConflictException("El nuevo email ya está en uso por otro registro");
        }

        // 3. Seteamos los nuevos valores
        s.setFullName(request.getFullName());
        s.setEmail(request.getEmail());
        s.setBirthDate(request.getBirthDate());

        // 4. Guardamos en MySQL y devolvemos la respuesta
        return toResponse(repo.save(s));
    }
}

