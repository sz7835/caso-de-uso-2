package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.entity.LiquidacionCronograma;

public interface LiquidacionCronogramaDao extends JpaRepository<LiquidacionCronograma, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM adm_liquidacion_cronogramas WHERE id_liquidacion = :id",nativeQuery = true)
    void deleteCronoLiqs(Long id);
}
