package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.ComProdServEstado;
import com.delta.deltanet.models.entity.ComProdServTipo;
import com.delta.deltanet.models.entity.ComProductoServicio;

public interface ComProductoServicioService {
  boolean existsDescripcionTipoActivo(String descripcion, Integer idTipo, Integer idExcluir);

  List<ComProductoServicio> findByParameters(int idTipo, int estado, String descripcion);

  Page<ComProductoServicio> findByParametersPaginated(int idTipo, int estado, String descripcion,
      Pageable pageable);

  ComProductoServicio saveProductoServicio(String descripcion, String createUser, int idTipo);

  ComProductoServicio findById(Integer id);

  ComProductoServicio updateProductoServicio(Integer id, String descripcion, String updateUser, int idTipo);

  ComProductoServicio activateProductoServicio(Integer id, String username);

  List<ComProdServTipo> findAllTipos();

  List<ComProdServEstado> findAllEstados();

}
