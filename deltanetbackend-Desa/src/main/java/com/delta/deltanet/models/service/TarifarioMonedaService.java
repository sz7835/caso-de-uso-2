package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TarifarioMoneda;
import java.util.List;

@SuppressWarnings("ALL")
public interface TarifarioMonedaService {

    TarifarioMoneda buscaId(Long id);

    List<TarifarioMoneda> findAll();

    TarifarioMoneda save(TarifarioMoneda moneda);

    List<TarifarioMoneda> findByNombreAndEstado(String nombre, int estado);
}
