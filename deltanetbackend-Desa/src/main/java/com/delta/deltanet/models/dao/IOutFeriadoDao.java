package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.OutFeriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IOutFeriadoDao extends JpaRepository<OutFeriado, Long> {
    @Query("select f " +
            "from OutFeriado f " +
            "where f.fecha = ?1")
    public OutFeriado buscaFecha(Date fecha);

    @Query("select f.fecha, f.descripcion " +
            "from OutFeriado f " +
            "where (f.fecha >= ?1 and f.fecha <= ?2) and esAplicableSectorPrivado = 'Y'")
    public List<Object> buscaRangoFecha(Date fecIni, Date fecFin);
    
    @Query("select f " +
            "from OutFeriado f " +
            "where f.fecha = ?1 and esAplicableSectorPrivado = 'Y'")
    public OutFeriado buscaFechaFeriado(Date fecha);

}
