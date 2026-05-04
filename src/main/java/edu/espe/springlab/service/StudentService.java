package edu.espe.springlab.service;

import edu.espe.springlab.dto.StudentCreateRequest;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.dto.StudentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {
    //Crear un ES
    StudentResponse create(StudentCreateRequest request);

    //Buscar ES por ID
    StudentResponse getById(Long id);

    //Listar todos los ES
    List<StudentResponse> list();

    //Cambiar estado
    StudentResponse deactivate(Long id);

    Page<StudentResponse> search(String name, int page, int size);

    StudentResponse update(Long id, StudentUpdateRequest request);
}
