package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAtencionDao extends JpaRepository<Atencion,Integer> {
    Optional<Atencion> findById(Integer id);
    List<Atencion> findByEstado(int estado);
    List<Atencion> findByIdTipoPer(int idTipoPer);

    List<Atencion> findByDescripAndIdTipoPerAndEstado(String descrip, int idTipoPer, int estado);
}
