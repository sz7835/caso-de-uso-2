package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteData;

import java.util.List;

public interface PostulanteDataService {

    List<PostulanteData> searchPostulantes(String nombre, Integer formacionAcademica, Integer gradoAcademico,
            Integer estado);

    String cambiarEstado(Long id, String usuario, int nuevoEstado);
}
