package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.entity.CatalogoServicio;
import com.delta.deltanet.models.entity.OrgAreas;

public interface ICatalogoServicioDao extends JpaRepository<CatalogoServicio, Long> {
	@Query("SELECT COUNT(c) FROM CatalogoServicio c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.area.id = :areaId AND c.estadoRegistro = 'A' AND (:idExcluir IS NULL OR c.id <> :idExcluir)")
	int countByNombreAndAreaIdAndActivoExcluyendoId(String nombre, Long areaId, Long idExcluir);

	@Query("from CatalogoServicio")
	public List<CatalogoServicio> findAll();

	public List<CatalogoServicio> findByArea(OrgAreas orgArea);

	public List<CatalogoServicio> findByAreaAndEstadoRegistro(OrgAreas orgArea, String Estado);

	public CatalogoServicio findByIdAndEstadoRegistro(Long id, String estado);

	@Query("from CatalogoServicio where area.id = ?1 and estadoRegistro = 'A'")
	public List<CatalogoServicio> findAllByArea(Long id);

	@Modifying
    @Transactional
    @Query(value = "DELETE FROM tkt_servicios_habilitado where ejecutor_id = :id", nativeQuery = true)
    void deleteService(Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tkt_servicios_habilitado (ejecutor_id, servicio_id) VALUES (:id, :idServicio)",
            nativeQuery = true)
    void insert(Long id, Long idServicio);
}
