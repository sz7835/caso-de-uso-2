package com.delta.deltanet.models.service;

import java.util.List;

// import com.delta.deltanet.models.entity.Area;
import com.delta.deltanet.models.entity.CatalogoServicio;
import com.delta.deltanet.models.entity.OrgAreas;

public interface ICatalogoServicioService {
	boolean existsNombreAreaActivo(String nombre, Long areaId, Long idExcluir);

	public List<CatalogoServicio> findAll();
	public List<CatalogoServicio> findByArea(OrgAreas orgArea);
	public List<CatalogoServicio> findByAreaAndEstado(OrgAreas orgArea, String estado);
	public CatalogoServicio findById(Long id);
	public CatalogoServicio findByIdAndEstado(Long id, String estado);
	public CatalogoServicio save(CatalogoServicio catalogoServicio);
	public void delete(Long id);
	public List<CatalogoServicio> findAllByArea(Long id);
	public void deleteService(Long id);
	public void insert(Long id, Long servicioID);

}
