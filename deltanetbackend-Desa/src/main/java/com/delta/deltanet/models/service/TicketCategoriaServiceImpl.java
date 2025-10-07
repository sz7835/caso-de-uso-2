package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.TicketCategoriaDao;
import com.delta.deltanet.models.entity.TicketCategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TicketCategoriaServiceImpl implements ITicketCategoriaService {

    @Override
    public boolean existsNombreActivo(String nombre, Long idExcluir) {
        if (nombre == null) return false;
        Long count = repository.countByNombreActivo(nombre);
        if (idExcluir != null) {
            TicketCategoria existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getNombre() != null && existente.getNombre().trim().equalsIgnoreCase(nombre.trim()) && existente.getEstado() == 'A') {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Autowired
    private TicketCategoriaDao repository;
    
    @Override
    public List<TicketCategoria> findAll() {
    	return repository.findAll();
    }
    
    @Override
    public TicketCategoria findById(Long id) {
    	return repository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public TicketCategoria save(TicketCategoria ticketCategoria) {
        return repository.save(ticketCategoria);
    }

    @Override
    @Transactional
    public TicketCategoria update(TicketCategoria ticketCategoria) {
        if (!repository.existsById(ticketCategoria.getId())) {
            throw new RuntimeException("No se encontró la categoría de ticket con ID: " + ticketCategoria.getId());
        }
        return repository.save(ticketCategoria);
    }

    @Override
    @Transactional
    public TicketCategoria changeEstado(Long id, char nuevoEstado, String username) {
    	TicketCategoria ticketCategoria = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el forma fact con ID: " + id));
    	
    	ticketCategoria.setUpdateUser(username);
    	ticketCategoria.setUpdateDate(new Date());
    	ticketCategoria.setEstado(nuevoEstado);
    	
        return repository.save(ticketCategoria);
    }
}
