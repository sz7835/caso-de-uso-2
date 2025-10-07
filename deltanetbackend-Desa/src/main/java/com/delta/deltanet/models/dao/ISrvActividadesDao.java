package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.ServicioActividades;

public interface ISrvActividadesDao extends JpaRepository<ServicioActividades, Long> {
	
	@Query("from ServicioActividades sa where sa.estado.id <> 7")
	List<ServicioActividades> listaActividades();
	
	@Query("select concat(sa.yearFac,'-',sa.mesFac) from ServicioActividades sa group by sa.mesFac, sa.yearFac order by sa.yearFac, sa.mesFac")
	List<String> listaPeriodos();
	
}
