package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SbsBanca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISbsBancaDao extends JpaRepository<SbsBanca,Integer> {
    int countByNombreAndEstado(String nombre, int estado);
    int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id);
        int countByCodsbsAndEstado(String codsbs, int estado);
        int countByCodsbsAndEstadoAndIdNot(String codsbs, int estado, Integer id);
    @Query("from SbsBanca where ctaSueldoFlg = 1 and estado = 1 order by nombre")
    List<SbsBanca> listaCtaSueldo();
}
