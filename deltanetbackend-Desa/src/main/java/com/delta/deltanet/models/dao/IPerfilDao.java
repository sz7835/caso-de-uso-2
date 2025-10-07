package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Perfil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPerfilDao extends JpaRepository<Perfil, Long> {

  List<Perfil> findAll();
}
