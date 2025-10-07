package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TrabajadorSalud;

public interface ITrabSaludService {
    public TrabajadorSalud save(TrabajadorSalud trabajadorSalud);
    public TrabajadorSalud findById(Long id);
    public TrabajadorSalud buscaByPerNat(Long idPerNat);
}
