package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TicketCategoria;

import java.util.List;

public interface ITicketCategoriaService {
	boolean existsNombreActivo(String nombre, Long idExcluir);
	public List<TicketCategoria> findAll();
	public TicketCategoria findById(Long id);
	public TicketCategoria save(TicketCategoria reg);
	public TicketCategoria update(TicketCategoria reg);
	public TicketCategoria changeEstado(Long id, char estado, String username);
}
