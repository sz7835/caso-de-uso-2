package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AutorizacionPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuthPerfilDao extends JpaRepository<AutorizacionPerfil,Integer> {
    AutorizacionPerfil findById(int id);
    AutorizacionPerfil findByNombre(String nombre);
    List<AutorizacionPerfil> findByEstado(Integer estado);
}
