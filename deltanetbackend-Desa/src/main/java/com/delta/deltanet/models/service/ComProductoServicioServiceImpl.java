package com.delta.deltanet.models.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ComProdServEstadoDao;
import com.delta.deltanet.models.dao.ComProdServTipoDao;
import com.delta.deltanet.models.dao.ComProductoServicioDao;
import com.delta.deltanet.models.entity.ComProdServEstado;
import com.delta.deltanet.models.entity.ComProdServTipo;
import com.delta.deltanet.models.entity.ComProductoServicio;

@Service
public class ComProductoServicioServiceImpl implements ComProductoServicioService {
  @Override
  public boolean existsDescripcionTipoActivo(String descripcion, Integer idTipo, Integer idExcluir) {
    int count = productoServicioDao.countByDescripcionAndIdTipoAndActivoExcluyendoId(descripcion, idTipo, idExcluir);
    return count > 0;
  }

  @Autowired
  private ComProductoServicioDao productoServicioDao;

  @Autowired
  private ComProdServTipoDao tipoRepository;

  @Autowired
  private ComProdServEstadoDao estadoRepository;

  @Override
  public List<ComProductoServicio> findByParameters(int idTipo, int estado, String descripcion) {
    return productoServicioDao.findByParameters(idTipo, estado, descripcion);
  }

  @Override
  public Page<ComProductoServicio> findByParametersPaginated(int idTipo, int estado, String descripcion,
      Pageable pageable) {
    return productoServicioDao.findByParametersPaginated(idTipo, estado, descripcion, pageable);
  }

  @Override
  public ComProductoServicio saveProductoServicio(String descripcion, String createUser, int idTipo) {
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede tener más de 100 caracteres.");
    }
    ComProductoServicio productoServicio = new ComProductoServicio();
    productoServicio.setDescripcion(descripcion);
    productoServicio.setEstado(1);
    productoServicio.setCreateUser(createUser);
    productoServicio.setCreateDate(new Date());
    productoServicio.setIdTipo(idTipo);
    return productoServicioDao.save(productoServicio);
  }

  @Override
  public ComProductoServicio findById(Integer id) {
    return productoServicioDao.findById(id).orElse(null);
  }

  @Override
  public ComProductoServicio updateProductoServicio(Integer id, String descripcion, String updateUser, int idTipo) {
    ComProductoServicio productoServicio = productoServicioDao.findById(id).orElse(null);
    if (productoServicio == null) {
      return null;
    }
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede tener más de 100 caracteres.");
    }
    productoServicio.setDescripcion(descripcion);
    productoServicio.setUpdateUser(updateUser);
    productoServicio.setUpdateDate(new Date());
    productoServicio.setIdTipo(idTipo);
    return productoServicioDao.save(productoServicio);
  }

  @Override
  public ComProductoServicio activateProductoServicio(Integer id, String username) {
    ComProductoServicio productoServicio = productoServicioDao.findById(id).orElse(null);
    if (productoServicio == null) {
      return null;
    }
    if (productoServicio.getEstado() == 2) {
      productoServicio.setEstado(1); // Activar
    } else {
      productoServicio.setEstado(2); // Desactivar
    }
    productoServicio.setUpdateUser(username);
    productoServicio.setUpdateDate(new Date());
    return productoServicioDao.save(productoServicio);
  }

  @Override
  public List<ComProdServTipo> findAllTipos() {
    return tipoRepository.findAll();
  }

  @Override
  public List<ComProdServEstado> findAllEstados() {
    return estadoRepository.findAll();
  }

}