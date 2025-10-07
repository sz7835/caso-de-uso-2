package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillDuraPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDuraPersDao extends JpaRepository<SkillDuraPers,Long> {
    @Query("select a.id, a.idPer, a.idDura,  c.descripcion, b.descripcion, d.descripcion, b.estado, d.id " +
            "from SkillDuraPers a " +
            "inner join a.persona p " +
            "inner join a.habDura b " +
            "inner join b.duraClas c " +
            "inner join a.habDuraFw d " +
            "where p.id = ?1 ")
    public List<Object> listaHabDuras(Long idPer);
}
