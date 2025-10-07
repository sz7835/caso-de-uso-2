package com.delta.deltanet.models.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.delta.deltanet.models.entity.TipoPersonal;

@Service
public class TipoPersonalServiceImpl implements ITipoPersonalService {
	
	@Override
	public List<TipoPersonal> findAll() {
		
		TipoPersonal tipo1 = new TipoPersonal();
		tipo1.setId(Long.valueOf(1));
		tipo1.setName("Interno");
		
		TipoPersonal tipo2 = new TipoPersonal();
		tipo2.setId(Long.valueOf(2));
		tipo2.setName("Externo");
		
		List<TipoPersonal> tipos = new ArrayList<>();
		tipos.add(tipo1);
		tipos.add(tipo2);
		
		return tipos;
	}

	
}
