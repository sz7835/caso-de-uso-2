package com.delta.deltanet.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.OutRegistroHoras;

@Repository
public interface OutRegistroHorasRepository extends JpaRepository<OutRegistroHoras, Integer> {

    @Query("SELECT r.dia, sum(r.horas) FROM OutRegistroHoras r " +
            "WHERE r.idPersona = :idPersona " +
            "AND r.dia >= :fechaInicio " +
            "AND r.dia <= :fechaFin " +
            "AND r.estado = :estado " +
            "group by r.dia order by r.dia asc")
    List<Object> findByPersonaCronogramaFechaEstado(@Param("idPersona") int idPersona, @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("estado") int estado);

        @Query("SELECT r.dia, r.horas, r.actividad FROM OutRegistroHoras r " +
            "WHERE r.idPersona = :idPersona " +
            "AND r.dia >= :fechaInicio " +
            "AND r.dia <= :fechaFin " +
            "AND r.estado = :estado " +
            "ORDER BY r.dia asc")
    List<Object> findByPersonaCronogramaFechaEstado2(@Param("idPersona") int idPersona, 
                                                   @Param("fechaInicio") Date fechaInicio, 
                                                   @Param("fechaFin") Date fechaFin, 
                                                   @Param("estado") int estado);

    // @Query("SELECT o FROM OutRegistroHoras o WHERE (:idPersona IS NULL OR o.idPersona = :idPersona) " +
    //         "AND (:idCronograma IS NULL OR o.idCronograma = :idCronograma) " +
    //         "AND (:meses = 99 OR MONTH(o.dia) = :meses) " +
    //         "AND (:estado = 9 OR o.estado = :estado)")
    // List<OutRegistroHoras> findByFiltros(@Param("idPersona") int idPersona, @Param("idCronograma") int idCronograma,
    //                                     @Param("meses") int meses, @Param("estado") int estado);

    @Query("SELECT o FROM OutRegistroHoras o WHERE "
    + "(:idPersona IS NULL OR o.idPersona = :idPersona) "
    + "AND (:estado IS NULL OR :estado = 9 OR o.estado = :estado) "
    + "AND (:fechaInicio IS NULL OR o.dia >= :fechaInicio) "
    + "AND (:fechaFin IS NULL OR o.dia <= :fechaFin)")
    List<OutRegistroHoras> findByFiltrosWithFechas(@Param("idPersona") Integer idPersona,
                                            @Param("estado") Integer estado,
                                            @Param("fechaInicio") Date fechaInicio,
                                            @Param("fechaFin") Date fechaFin);


    @Query("SELECT o FROM OutRegistroHoras o WHERE "
            + "(:idPersona IS NULL OR o.idPersona = :idPersona) "
            + "AND (:meses IS NULL OR :meses = 99 OR MONTH(o.dia) = :meses) "
            + "AND (:estado IS NULL OR :estado = 9 OR o.estado = :estado)")
    List<OutRegistroHoras> findByFiltrosWithMeses(@Param("idPersona") Integer idPersona,
                                                @Param("meses") Integer meses,
                                                @Param("estado") Integer estado);

    @Query("SELECT o FROM OutRegistroHoras o WHERE "
            + "(:idPersona IS NULL OR o.idPersona = :idPersona) "
            + "AND (:meses IS NULL OR :meses = 99 OR MONTH(o.dia) = :meses) "
            + "AND (:estado IS NULL OR :estado = 9 OR o.estado = :estado) "
            + "AND (:fechaInicio IS NULL OR o.dia >= STR_TO_DATE(:fechaInicio, '%Y/%m/%d')) "
            + "AND (:fechaFin IS NULL OR o.dia <= STR_TO_DATE(:fechaFin, '%Y/%m/%d'))")
    List<OutRegistroHoras> findByFiltrosWithMesesOrFechas(@Param("idPersona") Integer idPersona,
                                                        @Param("meses") Integer meses,
                                                        @Param("estado") Integer estado,
                                                        @Param("fechaInicio") String fechaInicio,
                                                        @Param("fechaFin") String fechaFin);
    

    @Query("SELECT o FROM OutRegistroHoras o WHERE "
            + "(:id IS NULL OR o.idProyecto = :id) ")
    List<OutRegistroHoras> findByIdProyecto(@Param("id") Integer id);

}