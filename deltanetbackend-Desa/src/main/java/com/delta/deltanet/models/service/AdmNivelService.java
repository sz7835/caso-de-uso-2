package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.AdmNivel;

public interface AdmNivelService {
  boolean existsDescripcionActivo(String descripcion, Integer idExcluir);
  AdmNivel save(AdmNivel nivel);
  List<AdmNivel> findByDescripcionAndEstado(String descripcion, int estado);
  Page<AdmNivel> findByDescripcionAndEstadoPaginated(String descripcion, int estado, Pageable pageable);
  AdmNivel saveNivel(String descripcion, String createUser);
  AdmNivel findById(Integer id);
  AdmNivel updateNivel(Integer id, String descripcion, String updateUser);
  AdmNivel activateNivel(Integer id, String username);
}
