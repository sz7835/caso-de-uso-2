package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.ISrvEstadoDao;
import com.delta.deltanet.models.entity.SrvEstado;

@Service
public class SrvEstadoServiceImpl implements ISrvEstadoService {
	@Autowired
	private ISrvEstadoDao repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long idExcluir) {
        if (nombre == null) return false;
        Long count = repository.countByNombreActivo(nombre);
        if (idExcluir != null) {
            SrvEstado existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getNombre() != null && existente.getNombre().trim().equalsIgnoreCase(nombre.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Override
    public SrvEstado findByPk(Long id) {
        return repository.findByPk(id);
    }

    @Override
    public List<SrvEstado> findAll() {
        return repository.findAll();
    }

    @Override
    public SrvEstado findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public SrvEstado save(SrvEstado srvEstado) {
        return repository.save(srvEstado);
    }

    @Override
    @Transactional
    public SrvEstado update(SrvEstado srvEstado) {
        if (!repository.existsById(srvEstado.getId())) {
            throw new RuntimeException("No se encontró srv estado con ID: " + srvEstado.getId());
        }
        return repository.save(srvEstado);
    }

    @Override
    @Transactional
    public SrvEstado changeEstado(Long id, Integer nuevoEstado, String username) {
        SrvEstado srvEstado = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró srv estado con ID: " + id));

        srvEstado.setUpdateUser(username);
        srvEstado.setUpdateDate(new Date());
        srvEstado.setEstado(Long.valueOf(nuevoEstado));

        return repository.save(srvEstado);
    }
}
