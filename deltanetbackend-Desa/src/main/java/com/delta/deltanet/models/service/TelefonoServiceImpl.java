package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITelefonoDao;
import com.delta.deltanet.models.entity.Telefono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefonoServiceImpl implements ITelefonoService {
    @Autowired
    private ITelefonoDao telefonoDao;


    @Override
    public List<Telefono> findAllFonos(Long id) {
        return telefonoDao.findAllFonos(id);
    }

    @Override
    public List<Object> findAllPer(Long idPersona) {
        return telefonoDao.findAllByPer(idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(Long tipo, String numero, Integer estado, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(tipo, numero, estado, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(Long tipo, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(tipo, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(Long tipo, String numero, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(tipo, numero, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(Long tipo, Integer estado, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(tipo, estado, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(String numero, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(numero, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(String numero, Integer estado, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(numero, estado, idPersona);
    }

    @Override
    public List<Object> findByTelefonosPer(Integer estado, Long idPersona) {
        return telefonoDao.findAllTelefonosPer(estado, idPersona);
    }

    @Override
    public Telefono findById(Long idTelefono) {
        return telefonoDao.findById(idTelefono);
    }

    @Override
    public Telefono save(Telefono telefono) {
        return telefonoDao.save(telefono);
    }

	@Override
	public boolean existsByNumero(String numero) {
		return telefonoDao.existsByNumero(numero);
	}

	@Override
	public boolean existsByNumeroAndPersona_IdNot(String numero, Long id) {
		return telefonoDao.existsByNumeroAndPersona_IdNot(numero, id);
	}
}
