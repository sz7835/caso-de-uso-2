package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CatalogoServicio;
import java.util.List;

public interface ICatalogoServicioCustomService {
    List<CatalogoServicio> findByFilters(Long areaId, String nombre, String estado);
}
