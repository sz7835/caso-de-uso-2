package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.AdmRequisito;

public interface AdmRequisitoService {
  boolean existsDescripcionActivo(String descripcion, Integer idExcluir);

  List<AdmRequisito> findByDescripcionAndEstado(String descripcion, int estado);

  Page<AdmRequisito> findByDescripcionAndEstadoPaginated(String descripcion, int estado, Pageable pageable);

  AdmRequisito saveRequisito(String descripcion, String createUser);

  AdmRequisito findById(Integer id);

  AdmRequisito updateRequisito(Integer id, String descripcion, String updateUser);

  AdmRequisito activateRequisito(Integer id, String username);
}