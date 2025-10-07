package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ICatalogoServicioDao;
import com.delta.deltanet.models.entity.CatalogoServicio;
import com.delta.deltanet.models.entity.OrgAreas;

@Service
public class CatalogoServicioServiceImpl implements ICatalogoServicioService {
	@Override
	public boolean existsNombreAreaActivo(String nombre, Long areaId, Long idExcluir) {
		int count = catalogoServicioDao.countByNombreAndAreaIdAndActivoExcluyendoId(nombre, areaId, idExcluir);
		return count > 0;
	}

	@Autowired
	private ICatalogoServicioDao catalogoServicioDao;

	@Override
	public List<CatalogoServicio> findAll() {
		return catalogoServicioDao.findAll();
	}

	@Override
	public CatalogoServicio findById(Long id) {
		return catalogoServicioDao.findById(id).orElse(null);
	}

	@Override
	public CatalogoServicio save(CatalogoServicio CatalogoServicio) {
		return catalogoServicioDao.save(CatalogoServicio);
	}

	@Override
	public void delete(Long id) {
		catalogoServicioDao.deleteById(id);
	}

	@Override
	public List<CatalogoServicio> findByArea(OrgAreas orgArea) {
		return catalogoServicioDao.findByArea(orgArea);
	}

	@Override
	public CatalogoServicio findByIdAndEstado(Long id, String estado) {
		return catalogoServicioDao.findByIdAndEstadoRegistro(id, estado);
	}

	@Override
	public List<CatalogoServicio> findByAreaAndEstado(OrgAreas area, String estado) {
		return catalogoServicioDao.findByAreaAndEstadoRegistro(area, estado);
	}

	@Override
	public List<CatalogoServicio> findAllByArea(Long id) {
		return catalogoServicioDao.findAllByArea(id);
	}

	@Override
	public void deleteService(Long id) {
		catalogoServicioDao.deleteService(id);
	}
	
	@Override
	public void insert(Long id, Long servicioID) {
		catalogoServicioDao.insert(id, servicioID);
	}
}
