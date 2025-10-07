package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.OutRegistroActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IOutRegistroActividadDAO extends JpaRepository<OutRegistroActividad,Long> {
    @Query("select ora.fecha, ora.registro, p.documento " +
            "from OutRegistroActividad ora " +
            "inner join ora.tipoActividad ta " +
            "inner join ora.persona p " +
            "where p.id = ?1 and (ora.fecha >= ?2 and ora.fecha <= ?3) and ta.id = ?4")
    public List<Object> BuscarPorRangoFechas(Long idPer, Date fecIni, Date fecFin, Long idTipoAct);

    @Query("select ora " +
            "from OutRegistroActividad ora " +
            "inner join ora.tipoActividad ta " +
            "inner join ora.persona p " +
            "where p.id = ?1 and ora.fecha = ?2 and ta.id = ?3")
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha, Long idTipoAct);

    @Query("select ora " +
            "from OutRegistroActividad ora " +
            "inner join ora.tipoActividad ta " +
            "inner join ora.persona p " +
            "where p.id = ?1 and ora.fecha = ?2")
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha);

    @Query("select ora " +
            "from OutRegistroActividad ora " +
            "inner join ora.tipoActividad ta " +
            "inner join ora.persona p " +
            "where p.id = ?1")
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer);
}
