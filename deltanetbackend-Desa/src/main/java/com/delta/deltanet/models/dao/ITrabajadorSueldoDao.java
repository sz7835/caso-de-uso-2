package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TrabajadorSueldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITrabajadorSueldoDao extends JpaRepository<TrabajadorSueldo,Long> {
    @Query("from TrabajadorSueldo where idPerNat = ?1")
    public TrabajadorSueldo buscaByPerNat(Long idPerNat);
}
