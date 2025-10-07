package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITipoDocumentoDao extends JpaRepository<TipoDocumento,Long> {
    @Query("from TipoDocumento where idTipDoc = ?1")
    TipoDocumento buscaTipoDoc(Long idTipoDoc);

    @Query("select t.longitud from TipoDocumento t where t.idTipDoc = ?1")
    Integer findLongitudByIdTipDoc(Long idTipDoc);
}
