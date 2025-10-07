package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PerfilDao;
import com.delta.deltanet.models.entity.Perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PerfilServiceImpl implements PerfilService {

    @Autowired
    private PerfilDao repository;

    @Override
    public List<Perfil> buscarPorNombreYEstado(String nombre, Integer estado) {
        return repository.findByNombreAndEstado(nombre, estado);
    }

    @Override
    public Perfil buscaId(Long idPer) {
        return repository.buscaId(idPer);
    }

    @Override
    public List<Perfil> findAll() {
    	return repository.findAll();
    }

    @Override
    public Perfil findById(Long id) {
    	return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Perfil save(Perfil perfil) {
        return repository.save(perfil);
    }

    @Override
    @Transactional
    public Perfil update(Perfil perfil) {
        if (!repository.existsById(perfil.getId())) {
            throw new RuntimeException("No se encontró el perfil con ID: " + perfil.getId());
        }
        return repository.save(perfil);
    }

    @Override
    @Transactional
    public Perfil changeEstado(Long id, Integer nuevoEstado, String username) {
    	Perfil perfil = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el perfil con ID: " + id));
    	
    	perfil.setUpdateUser(username);
    	perfil.setUpdateDate(new Date());
    	perfil.setEstado(nuevoEstado);
    	
        return repository.save(perfil);
    }
}
