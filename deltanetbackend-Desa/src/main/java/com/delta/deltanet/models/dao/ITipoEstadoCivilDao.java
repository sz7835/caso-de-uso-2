package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import com.delta.deltanet.models.entity.TipoEstadoCivil;

public interface ITipoEstadoCivilDao extends JpaRepository<TipoEstadoCivil, Long> {

}