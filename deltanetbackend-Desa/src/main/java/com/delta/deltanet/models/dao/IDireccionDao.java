package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDireccionDao extends JpaRepository<Direccion, Long> {

    @Query("from Direccion where id = ?1")
    Direccion buscaDir(Long idDireccion);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.persona.id = ?1")
    List<Object> findAllPer(Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.tipo = ?1 and d.direccion like %?2% and d.estado = ?3 and d.persona.id = ?4")
    List<Object> findByDirPer(Long tipo, String dire, int estado, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.tipo = ?1 and d.persona.id = ?2")
    List<Object> findByDirPer(Long tipo, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.tipo = ?1 and d.direccion like %?2% and d.persona.id = ?3")
    List<Object> findByDirPer(Long tipo, String dire, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.tipo = ?1 and d.estado = ?2 and d.persona.id = ?3")
    List<Object> findByDirPer(Long tipo, int estado, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.direccion like %?1% and d.persona.id = ?2")
    List<Object> findByDirPer(String dire, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.direccion like %?1% and d.estado = ?2 and d.persona.id = ?3")
    List<Object> findByDirPer(String dire, int estado, Long idPer);
    @Query("select d.id, d.persona.id, d.tipo, " +
            "d.direccion, ds.id, d.estado, d.createUser, " +
            "d.createDate, d.updateUser, d.updateDate " +
            "from Direccion d " +
            "inner join d.distrito ds " +
            "where d.estado = ?1 and d.persona.id = ?2")
    List<Object> findByDirPer2(int estado, Long idPer);

    @Query("select d.id, d.persona.id, d.tipo, " +
        "d.direccion, ds.id, d.estado, d.createUser, " +
        "d.createDate, d.updateUser, d.updateDate " +
        "from Direccion d " +
        "inner join d.distrito ds " +
        "where d.persona.id = ?1 and d.estado = 1 " +
        "order by d.tipo asc")
	List<Object> findAllPerWithEstado(Long idPer);

	@Query("SELECT d FROM Direccion d WHERE d.persona.id = :idPersona AND LOWER(d.direccion) = LOWER(:direccion)")
	List<Direccion> findByPersonaAndDireccion(@Param("idPersona") Long idPersona, @Param("direccion") String direccion);
}
