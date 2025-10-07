package com.delta.deltanet.models.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.AdmRequisitoDao;
import com.delta.deltanet.models.entity.AdmRequisito;

@Service
public class AdmRequisitoServiceImpl implements AdmRequisitoService {
  @Override
  public boolean existsDescripcionActivo(String descripcion, Integer idExcluir) {
    int count = requisitoDao.countByDescripcionActivoExcluyendoId(descripcion, idExcluir);
    return count > 0;
  }

  @Autowired
  private AdmRequisitoDao requisitoDao;

  @Override
  public List<AdmRequisito> findByDescripcionAndEstado(String descripcion, int estado) {
    return requisitoDao.findByDescripcionAndEstado(descripcion, estado);
  }

  @Override
  public Page<AdmRequisito> findByDescripcionAndEstadoPaginated(String descripcion, int estado, Pageable pageable) {
    return requisitoDao.findByDescripcionAndEstadoPaginated(descripcion, estado, pageable);
  }

  @Override
  public AdmRequisito saveRequisito(String descripcion, String createUser) {
    if (descripcion.length() > 200) {
      throw new IllegalArgumentException("La descripción no puede tener más de 200 caracteres.");
    }
    AdmRequisito requisito = new AdmRequisito();
    requisito.setDescripcion(descripcion);
    requisito.setEstado(1);
    requisito.setCreateUser(createUser);
    requisito.setCreateDate(new Date());
    return requisitoDao.save(requisito);
  }

  @Override
  public AdmRequisito findById(Integer id) {
    return requisitoDao.findById(id).orElse(null);
  }

  @Override
  public AdmRequisito updateRequisito(Integer id, String descripcion, String updateUser) {
    AdmRequisito requisito = requisitoDao.findById(id).orElse(null);
    if (requisito == null) {
      return null;
    }
    if (descripcion.length() > 200) {
      throw new IllegalArgumentException("La descripción no puede tener más de 200 caracteres.");
    }
    requisito.setDescripcion(descripcion);
    requisito.setUpdateUser(updateUser);
    requisito.setUpdateDate(new Date());
    return requisitoDao.save(requisito);
  }

  @Override
  public AdmRequisito activateRequisito(Integer id, String username) {
    AdmRequisito requisito = requisitoDao.findById(id).orElse(null);
    if (requisito == null) {
      return null;
    }
    if (requisito.getEstado() == 2) {
      requisito.setEstado(1); // Activar
    } else {
      requisito.setEstado(2); // Desactivar
    }
    requisito.setUpdateUser(username);
    requisito.setUpdateDate(new Date());
    return requisitoDao.save(requisito);
  }
}