package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.entity.ServicioHabilitado;
import com.delta.deltanet.models.entity.ServicioHabilitadoId;

public interface IServicioHabilitadoDao extends JpaRepository<ServicioHabilitado, ServicioHabilitadoId> {

    @Query("from ServicioHabilitado sh where sh.id.ejecutorId = ?1")
    List<ServicioHabilitado> buscarServicio(Integer id);

    @Query("from ServicioHabilitado sh where sh.id.servicioId = ?1")
    List<ServicioHabilitado> buscarporServicio(Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tkt_servicios_habilitado where ejecutor_id = :id", nativeQuery = true)
    void deleteService(Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tkt_servicios_habilitado (ejecutor_id, servicio_id) VALUES (:id, :idServicio)", nativeQuery = true)
    void insert(Long id, Long idServicio);
}
