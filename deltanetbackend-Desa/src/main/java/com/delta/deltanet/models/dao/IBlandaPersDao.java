package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillBlandaPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBlandaPersDao extends JpaRepository<SkillBlandaPers,Long> {
    @Query("select a.id, a.idPer, a.idBlanda,  c.descripcion, b.descripcion, b.estado " +
            "from SkillBlandaPers a " +
            "inner join a.persona p " +
            "inner join a.habBlanda b " +
            "inner join b.blandaClas c " +
            "where p.id = ?1 ")
    public List<Object> listaHabBlandas(Long idPer);
}
