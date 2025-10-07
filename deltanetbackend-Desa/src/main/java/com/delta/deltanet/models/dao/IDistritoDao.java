package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDistritoDao extends JpaRepository<Distrito,Long> {
    @Query("from Distrito where id = ?1")
    public Distrito findByIdDist(Long id);

    @Query("select d.id, d.nombre from Distrito d inner join d.provincia p where p.id = ?1")
    public List<Object> lstPorProv(Long idProv);
}
