package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PerNatSexo;
import java.util.List;

public interface IPerNatSexoService {
    List<PerNatSexo> findAll();
    PerNatSexo findById(Integer id);
}
