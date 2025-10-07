package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TrabajadorSueldo;

public interface ITrabSueldoService {
    public TrabajadorSueldo save (TrabajadorSueldo trabajadorSueldo);
    public TrabajadorSueldo findById(Long id);
    public TrabajadorSueldo buscaByPerNat(Long idPerNat);
}
