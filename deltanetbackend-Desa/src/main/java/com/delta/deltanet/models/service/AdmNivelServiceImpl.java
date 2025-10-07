package com.delta.deltanet.models.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.AdmNivelDao;
import com.delta.deltanet.models.entity.AdmNivel;

@Service
public class AdmNivelServiceImpl implements AdmNivelService {
  @Override
  public boolean existsDescripcionActivo(String descripcion, Integer idExcluir) {
  int count = nivelDao.countByDescripcionActivoExcluyendoId(descripcion, idExcluir);
  return count > 0;
  }

  @Override
  public AdmNivel save(AdmNivel nivel) {
    return nivelDao.save(nivel);
  }

  @Autowired
  private AdmNivelDao nivelDao;

  @Override
  public List<AdmNivel> findByDescripcionAndEstado(String descripcion, int estado) {
    return nivelDao.findByDescripcionAndEstado(descripcion, estado);
  }

  @Override
  public Page<AdmNivel> findByDescripcionAndEstadoPaginated(String descripcion, int estado, Pageable pageable) {
    return nivelDao.findByDescripcionAndEstadoPaginated(descripcion, estado, pageable);
  }

  @Override
  public AdmNivel saveNivel(String descripcion, String createUser) {
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede tener más de 100 caracteres.");
    }
    AdmNivel requisito = new AdmNivel();
    requisito.setDescripcion(descripcion);
    requisito.setEstado(1);
    requisito.setCreateUser(createUser);
    requisito.setCreateDate(new Date());
    return nivelDao.save(requisito);
  }

  @Override
  public AdmNivel findById(Integer id) {
    return nivelDao.findById(id).orElse(null);
  }

  @Override
  public AdmNivel updateNivel(Integer id, String descripcion, String updateUser) {
    AdmNivel requisito = nivelDao.findById(id).orElse(null);
    if (requisito == null) {
      return null;
    }
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede tener más de 100 caracteres.");
    }
    requisito.setDescripcion(descripcion);
    requisito.setUpdateUser(updateUser);
    requisito.setUpdateDate(new Date());
    return nivelDao.save(requisito);
  }

  @Override
  public AdmNivel activateNivel(Integer id, String username) {
    AdmNivel requisito = nivelDao.findById(id).orElse(null);
    if (requisito == null) {
      return null;
    }
    switch (requisito.getEstado()) {
      case 0:
        requisito.setEstado(1); // Activar
        break;
      case 1:
        requisito.setEstado(2); // Desactivar
        break;
      default:
        requisito.setEstado(2); // Desactivar por defecto
        break;
    }
    requisito.setUpdateUser(username);
    requisito.setUpdateDate(new Date());
    return nivelDao.save(requisito);
  }
}